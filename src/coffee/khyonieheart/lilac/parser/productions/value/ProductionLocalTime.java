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

import java.time.LocalTime;
import java.util.Optional;

import coffee.khyonieheart.lilac.parser.LilacDecoder;
import coffee.khyonieheart.lilac.value.TomlLocalTime;
import coffee.khyonieheart.lilac.value.TomlObject;

public class ProductionLocalTime
{
	public static Optional<TomlObject<?>> parse(
		LilacDecoder parser
	) {
		Optional<String> valueString = parser.parseRegex("(\\d{2}:\\d{2}\\d{2}.\\d+)");

		if (valueString.isEmpty())
		{
			return Optional.empty();
		}

		return Optional.of(new TomlLocalTime(LocalTime.parse(valueString.get())));
	}
}
