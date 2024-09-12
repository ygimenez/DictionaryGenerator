/*
 * This file is part of Shiro J Bot.
 * Copyright (C) 2019-2023  Yago Gimenez (KuuHaKu)
 *
 * Shiro J Bot is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Shiro J Bot is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Shiro J Bot.  If not, see <https://www.gnu.org/licenses/>
 */

package com.kuuhaku;

import org.apache.commons.lang3.tuple.Pair;

import java.util.List;

public class Uwuifier {
    private static final List<Pair<String, String>> exp = List.of(
            Pair.of("[rl]", "w"),
            Pair.of("[RL]", "W"),
            Pair.of("n([AEIOUaeiou])", "ny$1"),
            Pair.of("N([AEIOUaeiou])", "Ny$1"),
            Pair.of("ove", "uv")
    );

    public static String uwu(String word) {
        for (Pair<String, String> p : exp) {
            word = word.replaceAll(p.getLeft(), p.getRight());
        }

        return word;
    }
}