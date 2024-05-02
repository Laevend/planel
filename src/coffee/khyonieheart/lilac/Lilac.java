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

import coffee.khyonieheart.lilac.parser.LilacParser;

/**
 * Root class for the Lilac TOML library.
 *
 * @author Khyonie
 */
public class Lilac
{
	// Hide no-args constructor
	private Lilac() {}

	private static final int MAJOR_VERSION = 1;
	private static final int MINOR_VERSION = 4;
	private static final int PATCH_REVISION = 1;

	public static int getMajorVersion()
	{
		return MAJOR_VERSION;
	}

	public static int getMinorVersion()
	{
		return MINOR_VERSION;
	}

	public static int getRevision()
	{
		return PATCH_REVISION;
	}

	public static String getVersion()
	{
		return MAJOR_VERSION + "." + MINOR_VERSION + "." + PATCH_REVISION;
	}

	public static TomlParser tomlParser()
	{
		return new LilacParser();
	}

	/**
	 * Creates a new configuration builder.
	 *
	 * @return A new TOML configuration builder, to create a configuration
	 */
	public static TomlConfigurationBuilder newConfiguration()
	{
		return new TomlConfigurationBuilder();
	}
}
