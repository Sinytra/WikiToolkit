/*
 * Copyright (C) NeoForged 2024
 * 
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */

package org.moddedmc.wiki.toolkit;

import org.gradle.api.Named;
import org.jetbrains.annotations.Nullable;

import java.util.Locale;

// https://github.com/neoforged/ModDevGradle/blob/c5b119031493c0fd9253fc22da6a5965a8922e06/src/main/java/net/neoforged/moddevgradle/internal/utils/StringUtils.java
// https://github.com/neoforged/ModDevGradle/blob/c5b119031493c0fd9253fc22da6a5965a8922e06/src/main/java/net/neoforged/moddevgradle/dsl/InternalModelHelper.java
public class WikiUtils {

    public static String prefixTask(Named root, @Nullable String prefix, @Nullable String suffix) {
        return uncapitalize((prefix == null ? "" : prefix)
            + capitalize(root.getName())
            + (suffix == null ? "" : capitalize(suffix)));
    }

    private static String capitalize(String input) {
        if (input.isEmpty()) {
            return "";
        }
        return input.substring(0, 1).toUpperCase(Locale.ROOT) + input.substring(1);
    }

    private static String uncapitalize(String input) {
        if (input.isEmpty()) {
            return "";
        }
        return input.substring(0, 1).toLowerCase(Locale.ROOT) + input.substring(1);
    }
}
