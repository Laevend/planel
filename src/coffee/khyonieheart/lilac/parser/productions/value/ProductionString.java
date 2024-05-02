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

import java.util.Optional;

import coffee.khyonieheart.lilac.TomlSyntaxException;
import coffee.khyonieheart.lilac.api.StringType;
import coffee.khyonieheart.lilac.parser.LilacDecoder;
import coffee.khyonieheart.lilac.parser.productions.value.string.ProductionMultilineBasicString;
import coffee.khyonieheart.lilac.value.TomlObject;
import coffee.khyonieheart.lilac.value.TomlString;

public class ProductionString
{
	private static final String LEGAL_CHARS = "[^\u0000-\u0008\n-\u001F\u007F]"; // Regex that matches all but illegal chars

	private static final String BASIC_REGEX = "\"((?:\\\"|" + LEGAL_CHARS + ")*)\"";
	private static final String MULTILINE_LITERAL_REGEX = "'''(?:\\s*)?((?:'|\n|" + LEGAL_CHARS + ")*)'''";
	private static final String LITERAL_REGEX = "'(" + LEGAL_CHARS + "*?)'";

	private static final String LINE_ENDING_BACKSLASH_REGEX = "\\\n\\s*";

	public static Optional<TomlObject<?>> parse(
		LilacDecoder parser
	)
		throws TomlSyntaxException
	{
		Optional<String> stringOption = ProductionMultilineBasicString.parse(parser);
		if (stringOption.isPresent())
		{
			return Optional.of(new TomlString(stringOption.get(), StringType.MULTILINE_BASIC));
		}

		//stringOption = parser.parseRegex("\"((?:\\\"|.)*?)\"");
		stringOption = parser.parseRegex(BASIC_REGEX);
		if (stringOption.isPresent())
		{
			String finalString = stringOption.get().replaceAll(LINE_ENDING_BACKSLASH_REGEX, "");
			return Optional.of(new TomlString(finalString, StringType.BASIC));
		}

		stringOption = parser.parseRegex(MULTILINE_LITERAL_REGEX);
		if (stringOption.isPresent())
		{
			for (char c : stringOption.get().toCharArray())
			{
				if (c == '\n')
				{
					parser.nextLine();
				}
			}
			return Optional.of(new TomlString(stringOption.get(), StringType.MULTILINE_LITERAL));
		}

		stringOption = parser.parseRegex(LITERAL_REGEX);
		if (stringOption.isPresent())
		{
			return Optional.of(new TomlString(stringOption.get(), StringType.LITERAL));
		}

		return Optional.empty();
	}
}
