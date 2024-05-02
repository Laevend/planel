/*
 * This file is part of Lilac, a high performance TOML language library.
 * Copyright (C) 2024 Hailey-Jane "Khyonie" Garrett
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/>.
 */
package coffee.khyonieheart.lilac.parser.productions.value;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import coffee.khyonieheart.lilac.TomlSyntaxException;
import coffee.khyonieheart.lilac.api.Commentable;
import coffee.khyonieheart.lilac.parser.LilacDecoder;
import coffee.khyonieheart.lilac.parser.productions.ProductionComment;
import coffee.khyonieheart.lilac.parser.productions.ProductionValue;
import coffee.khyonieheart.lilac.value.TomlArray;
import coffee.khyonieheart.lilac.value.TomlObject;

public class ProductionArray
{
	/**
	 * [ {Value [,]} [,] ]
	 */
	public static Optional<TomlObject<?>> parse(
		LilacDecoder parser
	)
		throws TomlSyntaxException
	{
		if (!parser.parseLiteral("["))
		{
			return Optional.empty();
		}

		parser.toNextSymbol();

		Optional<String> commentOption = ProductionComment.parse(parser);
		while (commentOption.isPresent())
		{
			// TODO I'm not sure how to handle this, we'll discard comments for now
			parser.toNextSymbol();
			commentOption = ProductionComment.parse(parser);
		}

		List<TomlObject<?>> data = new ArrayList<>();
		Optional<TomlObject<?>> valueOption = ProductionValue.parse(parser, null);
		TomlObject<?> previousValue = null;
		while (valueOption.isPresent())
		{
			data.add(valueOption.get());
			previousValue = valueOption.get();

			parser.toNextSymbol();

			commentOption = ProductionComment.parse(parser);
			while (commentOption.isPresent())
			{
				if (valueOption.get() instanceof Commentable c)
				{
					c.setComment(commentOption.get());
				}

				parser.toNextSymbol();
				commentOption = ProductionComment.parse(parser);
			}

			if (parser.parseLiteral(","))
			{	
				parser.toNextSymbol();

				valueOption = ProductionValue.parse(parser, null);

				parser.toNextSymbol();
				continue;
			} 

			break;
		}

		commentOption = ProductionComment.parse(parser);
		if (commentOption.isPresent())
		{
			if (previousValue instanceof Commentable c)
			{
				c.setComment(commentOption.get());
			}

			parser.toNextSymbol();
		}

		parser.toNextSymbol();

		if (!parser.parseLiteral("]"))
		{
			throw new TomlSyntaxException("Expected a \"]\" to end array", parser.getLine(), parser.getLinePointer(), 1, parser.getCurrentDocument());
		}

		return Optional.of(new TomlArray(data));
	}
}
