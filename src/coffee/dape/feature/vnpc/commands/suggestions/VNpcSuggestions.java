package coffee.dape.feature.vnpc.commands.suggestions;

import java.util.Arrays;

import org.bukkit.entity.Villager;

import coffee.dape.Dape;
import coffee.dape.cmdparsers.astral.parser.Comparators;
import coffee.dape.cmdparsers.astral.suggestions.Suggestions;
import coffee.dape.cmdparsers.astral.suggestions.UnconditionalSuggestionList;
import coffee.dape.feature.vnpc.VNpc.InteractionType;
import coffee.dape.feature.vnpc.VNpcCtrl;
import coffee.dape.utils.structs.Namespace;

public class VNpcSuggestions
{
	public static UnconditionalSuggestionList vnpcs()
	{
		Namespace listNamespace = Namespace.of(Dape.getNamespaceName(),"vnpcs");
		if(Suggestions.hasSuggestionList(listNamespace)) { return Suggestions.get(listNamespace).asUnconditional(); }
		
		Suggestions.addSuggestionList(new UnconditionalSuggestionList(listNamespace,true,Comparators.ALPHABETICALLY)
		{
		    @Override
		    public void build()
		    {
		    	VNpcCtrl.getVNpcUIDs().forEach(uid -> add(uid.toString()));
		    }
		});
		
		return Suggestions.get(listNamespace).asUnconditional();
	}
	
	public static UnconditionalSuggestionList vnpcInteractionTypes()
	{
		Namespace listNamespace = Namespace.of(Dape.getNamespaceName(),"vnpc_interaction_types");
		if(Suggestions.hasSuggestionList(listNamespace)) { return Suggestions.get(listNamespace).asUnconditional(); }
		
		Suggestions.addSuggestionList(new UnconditionalSuggestionList(listNamespace,true,Comparators.ALPHABETICALLY)
		{
		    @Override
		    public void build()
		    {
		    	Arrays.stream(InteractionType.values()).forEach(v -> add(v.name().toLowerCase()));
		    }
		});
		
		return Suggestions.get(listNamespace).asUnconditional();
	}
	
	public static UnconditionalSuggestionList villagerProfessions()
	{
		Namespace listNamespace = Namespace.of(Dape.getNamespaceName(),"villager_professions");
		if(Suggestions.hasSuggestionList(listNamespace)) { return Suggestions.get(listNamespace).asUnconditional(); }
		
		Suggestions.addSuggestionList(new UnconditionalSuggestionList(listNamespace,true,Comparators.ALPHABETICALLY)
		{
		    @Override
		    public void build()
		    {
		    	Arrays.stream(Villager.Profession.values()).forEach(v -> add(v.name().toLowerCase()));
		    }
		});
		
		return Suggestions.get(listNamespace).asUnconditional();
	}
	
	public static UnconditionalSuggestionList villagerTypes()
	{
		Namespace listNamespace = Namespace.of(Dape.getNamespaceName(),"villager_types");
		if(Suggestions.hasSuggestionList(listNamespace)) { return Suggestions.get(listNamespace).asUnconditional(); }
		
		Suggestions.addSuggestionList(new UnconditionalSuggestionList(listNamespace,true,Comparators.ALPHABETICALLY)
		{
		    @Override
		    public void build()
		    {
		    	Arrays.stream(Villager.Type.values()).forEach(v -> add(v.name().toLowerCase()));
		    }
		});
		
		return Suggestions.get(listNamespace).asUnconditional();
	}
}
