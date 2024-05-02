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

import coffee.khyonieheart.lilac.parser.LilacDecoder;
import coffee.khyonieheart.lilac.value.TomlDouble;
import coffee.khyonieheart.lilac.value.TomlFloat;
import coffee.khyonieheart.lilac.value.TomlObject;

public class ProductionFloat
{
	public static Optional<TomlObject<?>> parse(
		LilacDecoder parser,
		String type
	) {
		Optional<String> floatOption = parser.parseRegex("([+-]?\\d+(?:_\\d+)*\\.\\d+(?:_\\d+)*(?:[eE][+-]?\\d+(?:_\\d+)*)?)");

		if (floatOption.isPresent())
		{
			return Optional.of(switch (type) {
				case "float" -> new TomlFloat(Float.parseFloat(floatOption.get().replace("_", "")));
				case "double" -> new TomlDouble(Double.parseDouble(floatOption.get().replace("_", "")));
				default -> throw new IllegalStateException("Invalid float type \"" + type + "\"");
			});
		}

		floatOption = parser.parseRegex("([+-]?\\d+(?:_\\d+)*[eE][+-]?\\d+(?:_\\d+)*)");
		if (floatOption.isPresent())
		{
			return Optional.of(switch (type) {
				case "float" -> new TomlFloat(Float.parseFloat(floatOption.get().replace("_", "")));
				case "double" -> new TomlDouble(Double.parseDouble(floatOption.get().replace("_", "")));
				default -> throw new IllegalStateException("Invalid float type \"" + type + "\"");
			});
		}

		// Otherwise check special float types
		if (parser.parseLiteral("inf") || parser.parseLiteral("+inf"))
		{
			return Optional.of(switch (type) {
				case "float" -> new TomlFloat(Float.POSITIVE_INFINITY);
				case "double" -> new TomlDouble(Double.POSITIVE_INFINITY);
				default -> throw new IllegalStateException("Invalid float type \"" + type + "\"");
			});
		}

		if (parser.parseLiteral("-inf"))
		{
			return Optional.of(switch (type) {
				case "float" -> new TomlFloat(Float.NEGATIVE_INFINITY);
				case "double" -> new TomlDouble(Double.NEGATIVE_INFINITY);
				default -> throw new IllegalStateException("Invalid float type \"" + type + "\"");
			});
		}

		if (parser.parseLiteral("nan") || parser.parseLiteral("+nan"))
		{
			return Optional.of(switch (type) {
				case "float" -> new TomlFloat(Float.NaN);
				case "double" -> new TomlDouble(Double.NaN);
				default -> throw new IllegalStateException("Invalid float type \"" + type + "\"");
			});
		}

		if (parser.parseLiteral("-nan"))
		{
			System.out.println("Warning: Java does not support the \"negative not-a-number\" float/double type. It has been converted to NaN.");
			return Optional.of(switch (type) {
				case "float" -> new TomlFloat(Float.NaN);
				case "double" -> new TomlDouble(Double.NaN);
				default -> throw new IllegalStateException("Invalid float type \"" + type + "\"");
			});
		}

		return Optional.empty();
	}
}
