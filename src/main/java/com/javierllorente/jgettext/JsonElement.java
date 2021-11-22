/*
 * Copyright (C) 2021 Javier Llorente <javier@opensuse.org>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.javierllorente.jgettext;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author javier
 */
public class JsonElement implements TranslationElement {

    private List<String> lines;
    private String tag;

    public JsonElement() {
        lines = new ArrayList<>();
    }

    @Override
    public List<String> get() {
        return lines;
    }

    @Override
    public void set(List<String> lines) {
        this.lines = lines;
    }

    @Override
    public void add(String line) {
        lines.add(line);
    }

    @Override
    public String getTag() {
        return tag;
    }

    @Override
    public void setTag(String tag) {
        this.tag = tag;
    }

    @Override
    public String toString() {
        String str = "";
        String line;

        for (int i = 0; i < lines.size(); i++) {
            line = lines.get(i);

            if (i > 0 && i != lines.size() - 1) {
                line += "\\n";
            }

            str += line;
        }

        return str;
    }
    
}
