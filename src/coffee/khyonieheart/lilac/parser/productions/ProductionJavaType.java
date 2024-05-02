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

import coffee.khyonieheart.lilac.parser.LilacDecoder;

public class ProductionJavaType
{
	public static Optional<String> parse(
		LilacDecoder parser
	) {
		parser.addStep("Parsing JavaType");
		if (parser.parseLiteral("byte"))
		{
			return Optional.of("byte");
		}

		if (parser.parseLiteral("short"))
		{
			return Optional.of("short");
		}

		if (parser.parseLiteral("integer"))
		{
			return Optional.of("integer");
		}

		if (parser.parseLiteral("long"))
		{
			return Optional.of("long");
		}

		if (parser.parseLiteral("float"))
		{
			return Optional.of("float");
		}

		if (parser.parseLiteral("double"))
		{
			return Optional.of("double");
		}

		return Optional.empty();
	}
}
