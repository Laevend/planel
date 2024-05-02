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

import coffee.khyonieheart.lilac.api.Commentable;

public interface TomlObject<T> extends Cloneable
{
	public T get();

	public TomlObjectType getType();

	public String serialize();

	public int getNumberOfTrailingNewlines();
	public void incrementTrailingNewlines();

	public default TomlObject<T> clone()
	{
		TomlObject<T> clone = this.clone();
		if (clone instanceof Commentable c)
		{
			c.setComment(((Commentable) this).getComment());
		}

		for (int i = 0; i < this.getNumberOfTrailingNewlines(); i++)
		{
			clone.incrementTrailingNewlines();
		}

		return clone;
	}
}
