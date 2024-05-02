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

import java.util.Optional;

import coffee.khyonieheart.lilac.TomlSyntaxException;
import coffee.khyonieheart.lilac.parser.LilacDecoder;

public class ProductionComment
{
	public static Optional<String> parse(
		LilacDecoder parser
	)
		throws TomlSyntaxException
	{
		if (!parser.parseLiteral("#"))
		{
			return Optional.empty();
		}

		StringBuilder commentBuilder = new StringBuilder();
		while (parser.charAtPointer() != '\n')
		{
			char pointerChar = parser.charAtPointer();

			if ((pointerChar >= 0x0000 && pointerChar <= 0x0008) ||
				(pointerChar >= 0x000A && pointerChar <= 0x001F) || 
				pointerChar == 0x007F)
			{
				throw new TomlSyntaxException("Illegal character in comment", parser.getLine(), parser.getLinePointer(), 1, parser.getCurrentDocument());
			}

			commentBuilder.append(pointerChar);
			parser.incrementPointer(1);

			if (parser.getPointer() == parser.getCurrentDocument().length())
			{
				break;
			}
		}

		return Optional.of(commentBuilder.toString());
	}
}
