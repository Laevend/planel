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

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import coffee.khyonieheart.lilac.adapter.TomlAdapter;
import coffee.khyonieheart.lilac.adapter.builtins.GenericTomlAdapter;
import coffee.khyonieheart.lilac.api.LilacExpose;
import coffee.khyonieheart.lilac.value.TomlObject;
import coffee.khyonieheart.lilac.value.TomlObjectType;
import coffee.khyonieheart.lilac.value.TomlTable;

/**
 * Combination of an encoder and decoder. Provides settings to configure encoding and decoding.
 *
 * @author Khyonie
 * @since 1.3.0
 */
public class TomlParser
{
	private final TomlEncoder encoder;
	private final TomlDecoder decoder;

	private boolean inlineTypes = false;
	private boolean preserveComments = true;
	private boolean uppercaseHex = true;
	private boolean separateArrayIntoLines = false;

	private boolean allowExperimental = false;

	private Map<Class<?>, TomlAdapter<?>> registeredTypeAdapters = new HashMap<>();

	public TomlParser(
		TomlEncoder encoder,
		TomlDecoder decoder
	) {
		this.encoder = Objects.requireNonNull(encoder);
		this.decoder = Objects.requireNonNull(decoder);
	}

	public TomlEncoder getEncoder() 
	{
		return encoder;
	}

	public TomlDecoder getDecoder() 
	{
		return decoder;
	}

	public boolean getStoreInlineTypes() 
	{
		return inlineTypes;
	}

	public TomlParser setStoreInlineTypes(
		boolean inlineTypes
	) {
		this.inlineTypes = inlineTypes;

		return this;
	}

	public boolean getPreserveComments() 
	{
		return preserveComments;
	}

	public TomlParser setPreserveComments(
		boolean preserveComments
	) {
		this.preserveComments = preserveComments;

		return this;
	}

	public boolean getIsUppercaseHex() 
	{
		return uppercaseHex;
	}

	public TomlParser setUppercaseHex(
		boolean uppercaseHex
	) {
		this.uppercaseHex = uppercaseHex;

		return this;
	}

	public boolean getSeparateArrayIntoLines() 
	{
		return separateArrayIntoLines;
	}

	public TomlParser setSeparateArrayIntoLines(
		boolean separateArrayIntoLines
	) {
		this.separateArrayIntoLines = separateArrayIntoLines;

		return this;
	}

	public TomlParser setExperimental(
		boolean allowExperimental
	) {
		this.allowExperimental = allowExperimental;

		return this;
	}

	public boolean getExperimental()
	{
		return this.allowExperimental;
	}

	public void registerTypeAdapter(
		Class<?> type,
		TomlAdapter<?> adapter
	) {
		Objects.requireNonNull(type);
		Objects.requireNonNull(adapter);

		this.registeredTypeAdapters.put(type, adapter);
	}

	public Map<String, TomlObject<?>> serializeToToml(
		Object object
	) {
		if (!allowExperimental)
		{
			throw new UnsupportedOperationException("Arbitrary object serialization is an experimental feature. Enable experimental features with TomlParser#setExperimental(true)");
		}

		Objects.requireNonNull(object);

		Map<String, TomlObject<?>> data = new LinkedHashMap<>();
		GenericTomlAdapter genericAdapter = new GenericTomlAdapter();

		// Public/inherited fields
		for (Field f : object.getClass().getFields())
		{
			if (!f.isAnnotationPresent(LilacExpose.class))
			{
				continue;
			}

			if (!f.getAnnotation(LilacExpose.class).serialize())
			{
				continue;
			}

			f.setAccessible(true);

			try {
				if (f.get(object) == null)
				{
					continue;
				}

				Object target = f.get(object);
				TomlObject<?> toml;

				if (this.registeredTypeAdapters.containsKey(target.getClass()))
				{
					toml = this.serializeInGenericContext(object, target.getClass());
					if (toml.getType() == TomlObjectType.TABLE)
					{
						((TomlTable) toml).setParents(new ArrayList<>(List.of(f.getName())));
					}

					data.put(f.getName(), toml);
					continue;
				}

				toml = genericAdapter.toToml(object);
				if (toml.getType() == TomlObjectType.TABLE)
				{
					((TomlTable) toml).setParents(new ArrayList<>(List.of(f.getName())));
				}

				data.put(f.getName(), toml);
			} catch (IllegalArgumentException | IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		
		// Otherwise inaccessable fields
		for (Field f : object.getClass().getDeclaredFields())
		{
			if (!f.isAnnotationPresent(LilacExpose.class))
			{
				continue;
			}

			if (!f.getAnnotation(LilacExpose.class).serialize())
			{
				continue;
			}

			f.setAccessible(true);

			try {
				if (f.get(object) == null)
				{
					continue;
				}

				Object target = f.get(object);
				TomlObject<?> toml;

				if (this.registeredTypeAdapters.containsKey(target.getClass()))
				{
					toml = this.serializeInGenericContext(object, target.getClass());
					if (toml.getType() == TomlObjectType.TABLE)
					{
						((TomlTable) toml).setParents(new ArrayList<>(List.of(f.getName())));
					}

					data.put(f.getName(), toml);
					continue;
				}

				toml = genericAdapter.toToml(object);
				if (toml.getType() == TomlObjectType.TABLE)
				{
					((TomlTable) toml).setParents(new ArrayList<>(List.of(f.getName())));
				}

				data.put(f.getName(), genericAdapter.toToml(object));
			} catch (IllegalArgumentException | IllegalAccessException e) {
				e.printStackTrace();
			}
		}

		return data;
	}

	@SuppressWarnings("unchecked")
	private <T> TomlObject<?> serializeInGenericContext(
		Object object,
		Class<T> type
	) {
		TomlAdapter<T> adapter = (TomlAdapter<T>) this.registeredTypeAdapters.get(type);
		return adapter.toToml((T) object);
	}

	@SuppressWarnings("unchecked")
	public <T> T deserializeFromToml(
		Map<String, TomlObject<?>> data,
		Class<T> type
	)
		throws InstantiationException,
			IllegalAccessException,
			InvocationTargetException,
			NoSuchMethodException
	{
		if (!allowExperimental)
		{
			throw new UnsupportedOperationException("Arbitrary object deserialization is an experimental feature. Enable experimental features with TomlParser#setExperimental(true)");
		}

		if (type.isInterface() || Modifier.isAbstract(type.getModifiers()))
		{
			if (this.registeredTypeAdapters.containsKey(type))
			{
				return (T) this.registeredTypeAdapters.get(type).fromToml(data);
			}

			throw new IllegalArgumentException("Cannot instantiate the abstract type " + type.getName() + ". Consider registering a type adapter for this type or using an implemented class");
		}

		if (this.registeredTypeAdapters.containsKey(type))
		{
			return (T) this.registeredTypeAdapters.get(type).fromToml(data);
		}

		return new GenericTomlAdapter().fromToml(data, type, this);
	}
}
