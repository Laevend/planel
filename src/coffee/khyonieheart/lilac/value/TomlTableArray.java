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
package coffee.khyonieheart.lilac.value;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class TomlTableArray implements TomlObject<List<Map<String, TomlObject<?>>>>
{
	private List<String> keys;
	private List<Map<String, TomlObject<?>>> data = new ArrayList<>();

	private Map<String, TomlObject<?>> targetMap;

	public TomlTableArray(
		List<String> keys
	) {
		this.keys = Objects.requireNonNull(keys);
		this.targetMap = new LinkedHashMap<>();
	}

	public void addToTarget(
		List<String> keys, 
		TomlObject<?> value
	) {
		String key = keys.get(keys.size() - 1);
		List<String> parents = keys.subList(0, keys.size() - 1);

		List<String> interimParents = new ArrayList<>();

		Map<String, TomlObject<?>> data = targetMap;
		for (String parent : parents)
		{
			if (!data.containsKey(parent))
			{
				data.put(parent, new TomlTable(interimParents));
			}

			if (!(data.get(parent) instanceof TomlTable))
			{
				throw new UnsupportedOperationException("Cannot redefine existing key " + parent + " with type " + data.get(parent).getType().name() + " as a table");
			}

			data = ((TomlTable) data.get(parent)).get();
		}

		data.put(key, value);
	}

	public boolean equalsTable(
		TomlTableArray tableArray
	) {
		if (tableArray == null)
		{
			return false;
		}

		return this.keysMatch(tableArray.keys);
	}

	private boolean keysMatch(
		List<String> keys
	) {
		if (this.keys.size() != keys.size())
		{
			return false;
		}

		for (int i = 0; i < this.keys.size(); i++)
		{
			if (!this.keys.get(i).equals(keys.get(i)))
			{
				return false;
			}
		}

		return true;
	}

	public void startNextTable()
	{
		targetMap = new LinkedHashMap<>();
		data.add(targetMap);
	}

	@Override
	public List<Map<String, TomlObject<?>>> get() 
	{
		return this.data;
	}

	public Map<String, TomlObject<?>> currentTable()
	{
		return this.targetMap;
	}

	@Override
	public TomlObjectType getType() 
	{
		return TomlObjectType.TABLE_ARRAY;
	}

	@Override
	public String serialize() 
	{
		StringBuilder builder = new StringBuilder();

		// TODO This

		return builder.toString();
	}

	@Override
	public int getNumberOfTrailingNewlines() 
	{
		throw new UnsupportedOperationException("Table arrays do not themselves contain newlines");
	}

	@Override
	public void incrementTrailingNewlines() 
	{
		throw new UnsupportedOperationException("Table arrays do not themselves contain newlines");
	}

	@Override
	public TomlTableArray clone()
	{
		List<Map<String, TomlObject<?>>> clone = new ArrayList<>();

		for (Map<String, TomlObject<?>> map : this.data)
		{
			Map<String, TomlObject<?>> mapClone = new LinkedHashMap<>();

			for (String key : map.keySet())
			{
				mapClone.put(key, map.get(key).clone());
			}

			clone.add(mapClone);
		}

		TomlTableArray newClone = new TomlTableArray(new ArrayList<>(this.keys));
		newClone.data = clone;

		return newClone;
	}
}
