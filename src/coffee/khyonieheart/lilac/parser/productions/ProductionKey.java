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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import coffee.khyonieheart.lilac.TomlSyntaxException;
import coffee.khyonieheart.lilac.parser.LilacDecoder;

public class ProductionKey
{
	private static final String BASIC_KEY_REGEX = "([A-Za-z0-9_-]+)";
	private static final String QUOTED_KEY_REGEX = "\"((?:\\\"|[^\"])*?)\"";
	private static final String LITERAL_KEY_REGEX = "'((?:\\'|.)*?)'";

	public static Optional<List<String>> parse(
		LilacDecoder parser
	) throws TomlSyntaxException
	{
		Optional<String> keyOption = parseKey(parser);

		if (keyOption.isEmpty())
		{
			return Optional.empty();
		}

		// Consume whitespace prior to dot
		while (parser.consumeCharacters(' ', '\t'));
		List<String> keys = new ArrayList<>();
		keys.add(keyOption.get());

		// Attempt to parse dot
		while (parser.parseLiteral("."))
		{
			while (parser.consumeCharacters(' ', '\t'));
			
			keyOption = parseKey(parser);
			if (keyOption.isEmpty())
			{
				throw new TomlSyntaxException("Expected a key after dot", parser.getLine(), parser.getLinePointer(), 1, parser.getCurrentDocument());
			}

			while (parser.consumeCharacters(' ', '\t'));
			keys.add(keyOption.get());
		}

		return Optional.of(keys);
	}

	private static Optional<String> parseKey(
		LilacDecoder parser
	) {
		Optional<String> keyOption = parser.parseRegex(BASIC_KEY_REGEX);
		if (keyOption.isPresent())
		{
			return keyOption;
		}

		keyOption = parser.parseRegex(QUOTED_KEY_REGEX);
		if (keyOption.isPresent())
		{
			return keyOption;
		}

		return parser.parseRegex(LITERAL_KEY_REGEX);
	}
}
