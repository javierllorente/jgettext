/*
 * Copyright (C) 2021-2022 Javier Llorente <javier@opensuse.org>
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

import com.javierllorente.jgettext.exception.UnsupportedFileFormatException;

/**
 *
 * @author javier
 */
public class ParserFactory {

    public TranslationParser getParser(String fileFormat) {
        if (fileFormat == null) {
            return null;
        }
        
        if (fileFormat.equalsIgnoreCase("po")) {
            return new PoParser();
        } else if(fileFormat.equalsIgnoreCase("json")) {
            return new JsonParser();
        } else {
            throw new UnsupportedFileFormatException(fileFormat);
        }
    }
}
