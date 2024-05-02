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
import coffee.khyonieheart.lilac.parser.productions.value.ProductionArray;
import coffee.khyonieheart.lilac.parser.productions.value.ProductionBoolean;
import coffee.khyonieheart.lilac.parser.productions.value.ProductionFloat;
import coffee.khyonieheart.lilac.parser.productions.value.ProductionInlineTable;
import coffee.khyonieheart.lilac.parser.productions.value.ProductionInteger;
import coffee.khyonieheart.lilac.parser.productions.value.ProductionLocalDate;
import coffee.khyonieheart.lilac.parser.productions.value.ProductionLocalDateTime;
import coffee.khyonieheart.lilac.parser.productions.value.ProductionLocalTime;
import coffee.khyonieheart.lilac.parser.productions.value.ProductionOffsetDateTime;
import coffee.khyonieheart.lilac.parser.productions.value.ProductionString;
import coffee.khyonieheart.lilac.value.TomlObject;

public class ProductionValue 
{
	public static Optional<TomlObject<?>> parse(
		LilacDecoder parser,
		String type
	)
		throws TomlSyntaxException
	{
		if (type != null)
		{
			Optional<TomlObject<?>> valueOption = ProductionInteger.parse(parser, type);
			if (valueOption.isPresent())
			{
				return valueOption;
			}

			valueOption = ProductionFloat.parse(parser, type);
			if (valueOption.isEmpty())
			{
				throw new TomlSyntaxException("Could not parse value with inline type \"" + type + "\"", parser.getLine(), parser.getLinePointer(), 1, parser.getCurrentDocument());
			}

			return valueOption;
		}
		
		Optional<TomlObject<?>> valueOption = ProductionString.parse(parser);
		if (valueOption.isPresent())
		{
			return valueOption;
		}

		valueOption = ProductionOffsetDateTime.parse(parser);
		if (valueOption.isPresent())
		{
			return valueOption;
		}

		valueOption = ProductionLocalDateTime.parse(parser);
		if (valueOption.isPresent())
		{
			return valueOption;
		}

		valueOption = ProductionLocalTime.parse(parser);
		if (valueOption.isPresent())
		{
			return valueOption;
		}

		valueOption = ProductionLocalDate.parse(parser);
		if (valueOption.isPresent())
		{
			return valueOption;
		}

		valueOption = ProductionFloat.parse(parser, "float");
		if (valueOption.isPresent())
		{
			return valueOption;
		}

		valueOption = ProductionInteger.parse(parser, "integer");
		if (valueOption.isPresent())
		{
			return valueOption;
		}

		valueOption = ProductionBoolean.parse(parser);
		if (valueOption.isPresent())
		{
			return valueOption;
		}

		valueOption = ProductionInlineTable.parse(parser);
		if (valueOption.isPresent())
		{
			return valueOption;
		}

		valueOption = ProductionArray.parse(parser);

		return valueOption;
	}
}
