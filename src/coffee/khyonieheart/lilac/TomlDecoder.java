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
package coffee.khyonieheart.lilac;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public interface TomlDecoder
{
	public TomlConfiguration decode(
		String document
	)
		throws TomlSyntaxException;

	public default TomlConfiguration decode(
		File file
	)
		throws FileNotFoundException,
			TomlSyntaxException
	{
		StringBuilder builder = new StringBuilder();

		try (Scanner scanner = new Scanner(file))
		{
			while (scanner.hasNext())
			{
				builder.append(scanner.nextLine());

				if (scanner.hasNext())
				{
					builder.append('\n');
				}
			}
		}

		return this.decode(builder.toString());
	}
}
