package coffee.dape.cmdparsers.astral.elevatedaccount;

import java.io.File;
import java.nio.file.Files;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Objects;

import coffee.dape.Dape;
import coffee.dape.exception.IllegalMethodCallException;
import coffee.dape.utils.Logg;
import coffee.dape.utils.clocks.RefillableIntervalClock;
import coffee.dape.utils.security.Bouncer;
import coffee.dape.utils.security.EncryptUtils;
import coffee.dape.utils.security.HashingUtils;
import coffee.dape.utils.security.ObfuscatedRandBaseEncoder;
import coffee.dape.utils.security.SecureByteArray;
import coffee.dape.utils.security.SecureString;

/**
 * 
 * @author Laeven
 * An elevated account specifically for console commands only
 */
public final class ConsoleAccount extends RefillableIntervalClock
{
	private SecureByteArray lastAuthDate = ObfuscatedRandBaseEncoder.encode(0L);
	private final SecureByteArray cooldownInMili = ObfuscatedRandBaseEncoder.encode(120_000L);	// 2 minutes
	private SecureByteArray locked;
	
	private PendingCommand pendingCommand = null;
	private SecureByteArray checksum = null;
	private SecureString hashedPin;
	private final SecureByteArray salt;
	private SecureByteArray attempt = ObfuscatedRandBaseEncoder.encode(1);
	private static final int maxAttempts = 5;
	
	protected ConsoleAccount()
	{
		// divided by 1000 as the cooldownInMili time here is in miliseconds but we want ticks so divide by 50 and add 1 as if cooldownInMili does not divide perfectly, we can't have part of a tick
		super("ConsoleAccountPendingCommandClear",(120_000L / 50));
		this.locked = ObfuscatedRandBaseEncoder.encode(ElevatedAccountCtrl.FALSE);
		this.salt = new SecureByteArray(HashingUtils.generateSalt());
	}
	
	protected ConsoleAccount(SecureString hash,byte[] salt)
	{
		super("ConsoleAccountPendingCommandClear",(120_000L / 50));
		this.locked = ObfuscatedRandBaseEncoder.encode(ElevatedAccountCtrl.FALSE);
		this.hashedPin = hash;
		this.salt = new SecureByteArray(salt);
	}

	public final SecureByteArray getLastAuthDate()
	{
		return lastAuthDate;
	}

	public final PendingCommand getPendingCommand()
	{
		return pendingCommand;
	}
	
	public final void setPendingCommand(PendingCommand pendingCommand) throws IllegalMethodCallException
	{
		Bouncer.haltAllBut(coffee.dape.cmdparsers.astral.parser.AstralExecutor.class);
		
		this.pendingCommand = pendingCommand;
		this.checksum = ObfuscatedRandBaseEncoder.encode(getCommandChecksum());
		Logg.verb("New Console Account Pending Command");
		if(isEnabled()) { refill(); return; }
		start();
	}
	
	public boolean hasPendingCommand()
	{
		return pendingCommand != null ? true : pendingCommand.getLabel() != null;
	}

	public final boolean isAuthed()
	{
		return (ObfuscatedRandBaseEncoder.peek(this.lastAuthDate) + ObfuscatedRandBaseEncoder.peek(cooldownInMili)) > System.currentTimeMillis();
	}
	
	protected final void setPin(final SecureString pin) throws IllegalMethodCallException
	{
		Bouncer.haltAllBut(coffee.dape.cmdparsers.astral.elevatedaccount.ElevatedAccountCtrl.class);
		this.hashedPin = new SecureString(EncryptUtils.toBase64(HashingUtils.hash(pin.asString(),this.salt.asByteArray())));
	}
	
	public final void auth(String value) throws IllegalMethodCallException
	{
		Bouncer.haltAllBut(coffee.dape.commands.ConsoleCommand.class);
		
		// Null checks
		Objects.requireNonNull(value,"ConsoleAccount auth Value cannot be null!");
		
		// Check if player has a pending command at all to execute
		if(!hasPendingCommand())
		{
			Logg.error("Console attempted to auth with no pending command!");
			return;
		}
		
		// Error feedback handled in verify method
		if(!verfiyPin(value))
		{
			// Check attempts on auth fail
			if(ObfuscatedRandBaseEncoder.peek(attempt) < maxAttempts) { return; }
			
			// Gone over max attempts, lock account. Account can only be unlocked by admin account
			locked = ObfuscatedRandBaseEncoder.encodeAndReplace(ElevatedAccountCtrl.TRUE,locked);
			
			// Reset attempts
			Logg.fatal("ConsoleAccount pin entered incorrectly " + maxAttempts + " times! Intruder suspected in console!");
			Dape.forceShutdown("ConsoleAccount pin entered too many times incorrectly!");
			return;
		}
		
		// Account owner is now authorised for elevated commands for the duration of 'cooldownInMili'
		this.lastAuthDate = ObfuscatedRandBaseEncoder.encodeAndReplace(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli(),this.lastAuthDate);
		
		// Reset attempts in authentication methods
		resetAttempt();
		
		long existingChecksum = ObfuscatedRandBaseEncoder.peek(checksum);
		long newChecksum = HashingUtils.getChecksum(pendingCommand.toString().getBytes());
		
		// Check command has not been modified via reflection
		if(existingChecksum != newChecksum)
		{
			Logg.error("Authentication Failed");
			Logg.error("Console attempted to auth with an account that had its pending command modified!");
			Dape.forceShutdown("Pending ConsoleAccount command had been modified in memory!");
			return;
		}
		
		// Execute command
		Logg.success("Authenticated!");
		pendingCommand.executeCommand();
		pendingCommand = new PendingCommand(null,null,null,null,null,new SecureRandom().nextLong());
		
		// Stop pending command removal clock, we've removed the the command ourselves
		if(isEnabled()) { stop(); return; }
	}
	
	private final boolean verfiyPin(final String value) throws IllegalMethodCallException
	{
		Bouncer.haltAllBut(coffee.dape.cmdparsers.astral.elevatedaccount.ConsoleAccount.class);
		
		if(value.length() > 32)
		{
			incrementAttempt();
			Logg.error("Authentication Failed, invalid pin!");
			return false;
		}
		
		String hashedPin = EncryptUtils.toBase64(HashingUtils.hash(value,this.salt.asByteArray()));		
		
		if(!hashedPin.equals(this.hashedPin.asString()))
		{
			incrementAttempt();
			Logg.error("Authentication Failed, invalid pin!");
			return false;
		}
		
		return true;
	}
	
	@Override
	public void execute() throws Exception
	{
		Bouncer.haltAllBut(coffee.dape.cmdparsers.astral.elevatedaccount.ConsoleAccount.class);
		
		// Clear the pending command when auth period expires to prevent a command loitering in memory
		pendingCommand = new PendingCommand(null,null,null,null,null,new SecureRandom().nextLong());
		Logg.verb("Pending command for ConsoleAccount was cleared automatically");
	}
	
	private final void incrementAttempt() throws IllegalMethodCallException
	{
		Bouncer.haltAllBut(coffee.dape.cmdparsers.astral.elevatedaccount.ConsoleAccount.class);
		attempt = ObfuscatedRandBaseEncoder.encodeAndReplace(ObfuscatedRandBaseEncoder.peek(attempt) + 1,attempt);
	}
	
	private final void resetAttempt() throws IllegalMethodCallException
	{
		Bouncer.haltAllBut(coffee.dape.cmdparsers.astral.elevatedaccount.ConsoleAccount.class);
		attempt = ObfuscatedRandBaseEncoder.encodeAndReplace(1,attempt);
	}
	
	private final long getCommandChecksum()
	{
		return HashingUtils.getChecksum(pendingCommand.toString().getBytes());
	}
	
	public final boolean isLocked()
	{
		return ObfuscatedRandBaseEncoder.peek(locked) == ElevatedAccountCtrl.TRUE;
	}

	public final static boolean isSetup()
	{
		return Files.exists(Dape.internalFilePath("elevated" + File.separator + "console"));
	}

	protected SecureString getHashedPin()
	{
		return hashedPin;
	}

	protected SecureByteArray getSalt()
	{
		return salt;
	}

	/**
	 * Called by garbage collector.
	 * <p>
	 * {@inheritDoc}
	 */
	@SuppressWarnings("removal")
	@Override
	public void finalize() throws Throwable
	{
		try { clear(); }
		finally { super.finalize(); }
	}
	
	/**
	 * Overwrite variables with garbage data when account is garbage collected
	 */
	public final void clear()
	{
		SecureRandom sr = new SecureRandom();
		
		byte[] authDateScrambled = new byte[sr.nextInt(16,32)];
		byte[] lockedScrambled = new byte[sr.nextInt(16,32)];
		
		sr.nextBytes(authDateScrambled);
		sr.nextBytes(lockedScrambled);
		
		// Remove obfuscated references from instances
		ObfuscatedRandBaseEncoder.remove(lastAuthDate);
		ObfuscatedRandBaseEncoder.remove(cooldownInMili);
		ObfuscatedRandBaseEncoder.remove(locked);
		ObfuscatedRandBaseEncoder.remove(checksum);
		ObfuscatedRandBaseEncoder.remove(salt);
		ObfuscatedRandBaseEncoder.remove(attempt);
		
		this.lastAuthDate = new SecureByteArray(authDateScrambled);
		this.pendingCommand = new PendingCommand(null,null,null,null,null,sr.nextLong());
		this.locked = new SecureByteArray(lockedScrambled);
	}
}
