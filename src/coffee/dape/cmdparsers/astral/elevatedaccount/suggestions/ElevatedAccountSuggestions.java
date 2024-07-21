package coffee.dape.cmdparsers.astral.elevatedaccount.suggestions;

import coffee.dape.Dape;
import coffee.dape.cmdparsers.astral.elevatedaccount.ElevatedAccountCtrl;
import coffee.dape.cmdparsers.astral.parser.Comparators;
import coffee.dape.cmdparsers.astral.suggestions.Suggestions;
import coffee.dape.cmdparsers.astral.suggestions.UnconditionalSuggestionList;
import coffee.dape.utils.PlayerUtils;
import coffee.dape.utils.structs.Namespace;

public class ElevatedAccountSuggestions
{
	public static UnconditionalSuggestionList elevatedAccountOwners()
	{
		Namespace listNamespace = Namespace.of(Dape.getNamespaceName(),"elevated_accounts");
		if(Suggestions.hasSuggestionList(listNamespace)) { return Suggestions.get(listNamespace).asUnconditional(); }
		
		Suggestions.addSuggestionList(new UnconditionalSuggestionList(listNamespace,true,Comparators.ALPHABETICALLY)
		{
		    @Override
		    public void build()
		    {
		    	ElevatedAccountCtrl.getAccounts().keySet().forEach(accId -> add(PlayerUtils.getName(accId)));
		    }
		});
		
		return Suggestions.get(listNamespace).asUnconditional();
	}
}
