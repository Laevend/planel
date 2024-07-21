package coffee.dape.cmdparsers.astral.elevatedaccount;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import coffee.dape.cmdparsers.astral.parser.AstralExecutor;
import coffee.dape.utils.StringUtils;

/**
 * @author Laeven
 */
public class PendingCommand
{
	private final CommandSender sender;
	private final Command command;
	private final String label;
	private final String[] args;	
	private final AstralExecutor executor;
	private final long timeCommandIssuedMili;
	
	public PendingCommand(CommandSender sender,Command cmd,String label,String[] args,AstralExecutor executor,long timeCommandIssuedMili)
	{
		this.sender = sender;
		this.command = cmd;
		this.label = label;
		this.args = args;
		this.executor = executor;
		this.timeCommandIssuedMili = timeCommandIssuedMili; 
	}
	
	public final void executeCommand()
	{
		if(executor == null) { return; }
		executor.onCommand(sender,command,label,args);
	}

	public final long getTimeCommandIssuedMili()
	{
		return timeCommandIssuedMili;
	}
	
	public final String getLabel()
	{
		return label;
	}
	
	@Override
	public String toString()
	{
		return "[Sender: " + sender.getName() + "," +
				"Command: " + command.getName() + "," +
				"Label: " + label + "," +
				"Args: {" + StringUtils.arrayToCSV(args) + "}," +
				"Executor: " + executor.getCommandClass().toString() + "," +
				"TimeCommandIssuedMili: " + timeCommandIssuedMili + "]";
	}
}
