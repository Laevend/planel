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
package coffee.khyonieheart.lilac.adapter.builtins;

import coffee.khyonieheart.lilac.value.TomlBoolean;
import coffee.khyonieheart.lilac.value.TomlByte;
import coffee.khyonieheart.lilac.value.TomlDouble;
import coffee.khyonieheart.lilac.value.TomlFloat;
import coffee.khyonieheart.lilac.value.TomlInteger;
import coffee.khyonieheart.lilac.value.TomlLong;
import coffee.khyonieheart.lilac.value.TomlObject;
import coffee.khyonieheart.lilac.value.TomlShort;

public class PrimitiveTomlAdapter
{
	public static TomlObject<?> toToml(
		Object object
	) {
		return switch (object.getClass().getSimpleName())
		{
			case "byte" -> new TomlByte((byte) object);
			case "short" -> new TomlShort((short) object);
			case "int" -> new TomlInteger((int) object);
			case "long" -> new TomlLong((long) object);
			case "float" -> new TomlFloat((float) object);
			case "double" -> new TomlDouble((double) object);
			case "boolean" -> new TomlBoolean((boolean) object);
			case "char" -> throw new UnsupportedOperationException("TOML does not support character literals");
			default -> throw new IllegalArgumentException("Input object must be a primitive");
		};
	}
}
