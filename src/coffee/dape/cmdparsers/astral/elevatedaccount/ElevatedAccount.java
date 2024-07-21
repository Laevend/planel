package coffee.dape.cmdparsers.astral.elevatedaccount;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import org.bukkit.entity.Player;

import coffee.dape.Dape;
import coffee.dape.cmdparsers.astral.elevatedaccount.authmethod.AuthenticationMethod;
import coffee.dape.exception.IllegalMethodCallException;
import coffee.dape.utils.Logg;
import coffee.dape.utils.MathUtils;
import coffee.dape.utils.PlayerUtils;
import coffee.dape.utils.PrintUtils;
import coffee.dape.utils.clocks.RefillableIntervalClock;
import coffee.dape.utils.security.Bouncer;
import coffee.dape.utils.security.HashingUtils;
import coffee.dape.utils.security.ObfuscatedRandBaseEncoder;
import coffee.dape.utils.security.SecureByteArray;

public final class ElevatedAccount extends RefillableIntervalClock
{
	private final UUID owner;
	
	private final SecureByteArray creationDate;
	private SecureByteArray lastAuthDate = ObfuscatedRandBaseEncoder.encode(0L);
	private final SecureByteArray cooldownInMili;
	private SecureByteArray locked;
	private SecureByteArray markedAsDeleted;
	private final List<AuthenticationMethod> authMethods;
	
	private PendingCommand pendingCommand = null;
	private SecureByteArray authLvl = ObfuscatedRandBaseEncoder.encode(0);
	private SecureByteArray checksum = null;
	
	protected ElevatedAccount(final UUID owner,final List<AuthenticationMethod> authMethods)
	{
		// divided by 1000 as the cooldownInMili time here is in miliseconds but we want ticks so divide by 50 and add 1 as if cooldownInMili does not divide perfectly, we can't have part of a tick
		super("ElevatedAccount_" + PlayerUtils.getName(owner),(Dape.getConfigFile().getLong(ElevatedAccountCtrl.ConfigKey.AUTH_TIME) / 50) + 1);
		this.owner = owner;
		this.creationDate = ObfuscatedRandBaseEncoder.encode(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());
		this.cooldownInMili = ObfuscatedRandBaseEncoder.encode(Dape.getConfigFile().getLong(ElevatedAccountCtrl.ConfigKey.AUTH_TIME));
		this.authMethods = Collections.unmodifiableList(authMethods);
		this.locked = ObfuscatedRandBaseEncoder.encode(ElevatedAccountCtrl.FALSE);
		this.markedAsDeleted = ObfuscatedRandBaseEncoder.encode(ElevatedAccountCtrl.FALSE);
	}
	
	protected ElevatedAccount(final UUID owner,final long creationDate,final boolean locked,final List<AuthenticationMethod> authMethods)
	{
		// divided by 1000 as the cooldownInMili time here is in miliseconds but we want ticks so divide by 50 and add 1 as if cooldownInMili does not divide perfectly, we can't have part of a tick
		super("ElevatedAccount_" + PlayerUtils.getName(owner),(Dape.getConfigFile().getLong(ElevatedAccountCtrl.ConfigKey.AUTH_TIME) / 50) + 1);
		this.owner = owner;
		this.creationDate = ObfuscatedRandBaseEncoder.encode(creationDate);
		this.cooldownInMili = ObfuscatedRandBaseEncoder.encode(Dape.getConfigFile().getLong(ElevatedAccountCtrl.ConfigKey.AUTH_TIME));
		this.authMethods = Collections.unmodifiableList(authMethods);
		this.locked = locked ? ObfuscatedRandBaseEncoder.encode(ElevatedAccountCtrl.TRUE) : ObfuscatedRandBaseEncoder.encode(ElevatedAccountCtrl.FALSE);
		this.markedAsDeleted = ObfuscatedRandBaseEncoder.encode(ElevatedAccountCtrl.FALSE);
	}

	public final UUID getOwner()
	{
		return owner;
	}

	public final SecureByteArray getCreationDate()
	{
		return creationDate;
	}

	public final SecureByteArray getLastAuthDate()
	{
		return lastAuthDate;
	}

	public final PendingCommand getPendingCommand()
	{
		return pendingCommand;
	}
	
	public void setPendingCommand(PendingCommand pendingCommand) throws IllegalMethodCallException
	{
		Bouncer.haltAllBut(coffee.dape.cmdparsers.astral.parser.AstralExecutor.class);
		
		this.pendingCommand = pendingCommand;
		this.checksum = ObfuscatedRandBaseEncoder.encode(getCommandChecksum());
		if(isEnabled()) { refill(); return; }
		start();
	}
	
	public boolean hasPendingCommand()
	{
		return pendingCommand == null ? false : pendingCommand.getLabel() == null ? false : true;
	}

	public final boolean isAuthed()
	{
		return (ObfuscatedRandBaseEncoder.peek(this.lastAuthDate) + ObfuscatedRandBaseEncoder.peek(cooldownInMili)) > System.currentTimeMillis();
	}
	
	public final void auth(String value,Player p) throws IllegalMethodCallException
	{
		Bouncer.haltAllBut(coffee.dape.commands.AuthCommand.class);
		
		// Null checks
		Objects.requireNonNull(value,"ElevatedAccount auth Value cannot be null!");
		Objects.requireNonNull(p,"ElevatedAccount auth Player cannot be null!");
		
		// Check if player has a pending command at all to execute
		if(!hasPendingCommand())
		{
			PrintUtils.info(p,"You have no pending command that needs authorising.");
			Logg.error("User " + p.getName() + " attempted to auth with no pending command!");
			return;
		}
		
		// Check if a different user is somehow using someone elses account?
		if(!p.getUniqueId().equals(owner))
		{
			PrintUtils.error(p,"Authentication Failed");
			Logg.error("User " + p.getName() + " attempted to auth " + 
					(pendingCommand == null ? "(no pending cmd)" : ("'/" + pendingCommand.getLabel() + "'")) + " using account owned by " + 
					PlayerUtils.getName(owner));
			return;
		}
		
		// Check if this elevated account has at least 1 authentication method
		// Why are you using an elevated account if you don't provide an authentication method? It's akin to clicking 'yes' on a UAC prompt
		if(authMethods == null || authMethods.size() == 0)
		{
			PrintUtils.error(p,"Authentication Failed");
			Logg.error("User " + p.getName() + " attempted to auth with an account that has 0 authentication methods!");
			return;
		}
		
		long authLvl = ObfuscatedRandBaseEncoder.decode(this.authLvl);
		
		// Notify admin of an invalid auth level. Might indicate another plugin was attempting to force authentication
		if(authLvl >= authMethods.size() || authLvl < 0)
		{
			Logg.error("User " + p.getName() + " attempted to auth with an account that has an invalid AuthLvl! (" + authLvl + ")");
			this.authLvl = ObfuscatedRandBaseEncoder.encode(0);
			return;
		}
		
		// clamp auth level
		authLvl = authMethods.size() == 1 ? 0 : MathUtils.clamp(0,authMethods.size() - 1,authLvl);
		
		AuthenticationMethod meth = authMethods.get((int) authLvl);
		
		// Error feedback handled in verify method
		if(!meth.verifyMethod(value,p))
		{
			// Re-encode auth level
			this.authLvl = ObfuscatedRandBaseEncoder.encode(authLvl);
			
			// Check attempts on auth fail
			if(ObfuscatedRandBaseEncoder.peek(meth.getAttempt()) < meth.maxAttempts()) { return; }
			
			// Gone over max attempts, lock account. Account can only be unlocked by admin account
			locked = ObfuscatedRandBaseEncoder.encodeAndReplace(ElevatedAccountCtrl.TRUE,locked);
			
			// Reset attempts
			for(AuthenticationMethod method : authMethods)
			{
				method.resetAttempt();
			}
			
			return;
		}
		
		// Check if the current auth was the last auth required
		if(authLvl == (authMethods.size() - 1))
		{
			// Account owner is now authorised for elevated commands for the duration of 'cooldownInMili'
			this.lastAuthDate = ObfuscatedRandBaseEncoder.encodeAndReplace(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli(),this.lastAuthDate);
			this.authLvl = ObfuscatedRandBaseEncoder.encodeAndReplace(0,this.authLvl);
			
			// Reset attempts in authentication methods
			for(AuthenticationMethod method : authMethods)
			{
				method.resetAttempt();
			}
			
			long existingChecksum = ObfuscatedRandBaseEncoder.peek(checksum);
			long newChecksum = HashingUtils.getChecksum(pendingCommand.toString().getBytes());
			
			// Check command has not been modified via reflection
			if(existingChecksum != newChecksum)
			{
				PrintUtils.error(p,"Authentication Failed");
				Logg.error("User " + p.getName() + " attempted to auth with an account that had its pending command modified!");
				return;
			}
			
			// Execute command
			PrintUtils.success(p,"Authenticated!");
			pendingCommand.executeCommand();
			pendingCommand = new PendingCommand(null,null,null,null,null,new SecureRandom().nextLong());
			
			// Stop pending command removal clock, we've removed the the command ourselves
			if(isEnabled()) { stop(); return; }
			return;
		}
		
		// Another authentication method exists, provide user with input request for this authentication method
		authLvl++;
		this.authLvl = ObfuscatedRandBaseEncoder.encode(authLvl);
		PrintUtils.info(p,authMethods.get((int) authLvl).getAuthMessage());
	}
	
	@Override
	public void execute() throws Exception
	{
		Bouncer.haltAllBut(coffee.dape.cmdparsers.astral.elevatedaccount.ElevatedAccount.class);
		
		// Clear the pending command when auth period expires to prevent a command loitering in memory
		pendingCommand = new PendingCommand(null,null,null,null,null,new SecureRandom().nextLong());
		Logg.verb("Pending command for ElevatedAccount " + PlayerUtils.getName(owner) + " was cleared automatically");
	}
	
	private final long getCommandChecksum()
	{
		return HashingUtils.getChecksum(pendingCommand.toString().getBytes());
	}
	
	public final boolean isLocked()
	{
		return ObfuscatedRandBaseEncoder.peek(locked) == ElevatedAccountCtrl.TRUE;
	}
	
	/**
	 * Locks the account
	 */
	public final void lock()
	{
		locked = ObfuscatedRandBaseEncoder.encodeAndReplace(ElevatedAccountCtrl.TRUE,locked);
	}
	
	/**
	 * Unlocks the account
	 * @throws IllegalMethodCallException 
	 */
	public final void unlock() throws IllegalMethodCallException
	{
		Bouncer.haltAllBut(coffee.dape.commands.ConsoleCommand.class);
		locked = ObfuscatedRandBaseEncoder.encodeAndReplace(ElevatedAccountCtrl.FALSE,locked);
	}

	protected void markAsDeleted(boolean markDeleted)
	{
		this.markedAsDeleted = ObfuscatedRandBaseEncoder.encodeAndReplace(markDeleted ? ElevatedAccountCtrl.TRUE : ElevatedAccountCtrl.FALSE,this.markedAsDeleted);
	}

	/**
	 * Elevated accounts cannot be deleted or added once the server is online as they
	 * exist in a final unmodifiable map. Marking an account as deleted is the only way to remove an account
	 * @return
	 */
	public final boolean isMarkedAsDeleted()
	{
		return ObfuscatedRandBaseEncoder.peek(markedAsDeleted) == ElevatedAccountCtrl.TRUE;
	}
	
	public List<AuthenticationMethod> getAuthMethods()
	{
		return authMethods;
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
		ObfuscatedRandBaseEncoder.remove(creationDate);
		ObfuscatedRandBaseEncoder.remove(lastAuthDate);
		ObfuscatedRandBaseEncoder.remove(cooldownInMili);
		ObfuscatedRandBaseEncoder.remove(locked);
		ObfuscatedRandBaseEncoder.remove(markedAsDeleted);
		ObfuscatedRandBaseEncoder.remove(authLvl);
		ObfuscatedRandBaseEncoder.remove(checksum);
		
		this.lastAuthDate = new SecureByteArray(authDateScrambled);
		this.pendingCommand = new PendingCommand(null,null,null,null,null,sr.nextLong());
		this.locked = new SecureByteArray(lockedScrambled);
		
		for(AuthenticationMethod meth : authMethods)
		{
			meth.clear();
		}
	}
}
