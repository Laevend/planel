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
package coffee.khyonieheart.lilac.parser.productions;

import java.util.List;
import java.util.Optional;

import coffee.khyonieheart.lilac.TomlSyntaxException;
import coffee.khyonieheart.lilac.parser.LilacDecoder;
import coffee.khyonieheart.lilac.value.TomlTable;

public class ProductionDiscreteTable
{
	/**
	 * [ Key {. Key} ] [Comment] {Newline}
	 */
	public static Optional<TomlTable> parse(
		LilacDecoder parser
	)
		throws TomlSyntaxException
	{
		if (!parser.parseLiteral("["))
		{
			return Optional.empty();
		}

		parser.toNextSymbol();

		Optional<List<String>> keys = ProductionKey.parse(parser);

		if (keys.isEmpty())
		{
			throw new TomlSyntaxException("A table must explicitly define at least one key", parser.getLine(), parser.getLinePointer(), 1, parser.getCurrentDocument());
		}

		while (parser.consumeCharacters(' ', '\t'));

		if (!parser.parseLiteral("]"))
		{
			throw new TomlSyntaxException("Expected a \"]\" to end DiscreteTable", parser.getLine(), parser.getLinePointer(), 1, parser.getCurrentDocument());
		}

		List<String> parents = keys.get().subList(0, keys.get().size() - 1);
		String key = keys.get().get(keys.get().size() - 1);

		TomlTable table = new TomlTable(key, parents);

		while (parser.consumeCharacters(' ', '\t'));
		Optional<String> comment = ProductionComment.parse(parser);
		if (comment.isPresent())
		{
			table.setComment(comment.get());
		}

		parser.clearCurrentTableArray();

		return Optional.of(table);
	}
}
