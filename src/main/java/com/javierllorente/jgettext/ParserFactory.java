/*
 * Copyright (C) 2021-2023 Javier Llorente <javier@opensuse.org>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.javierllorente.jgettext;

import com.javierllorente.jgettext.exception.UnsupportedFileFormatException;

/**
 *
 * @author javier
 */
public class ParserFactory implements TranslationParserFactory {

    @Override
    public TranslationParser getParser(String fileFormat) {
        if (fileFormat == null) {
            return null;
        }
        
        if (fileFormat.equalsIgnoreCase("po")) {
            return new PoParser();
        } else if (fileFormat.equalsIgnoreCase("json")) {
            return new JsonParser();
        } else if (fileFormat.equalsIgnoreCase("yaml")) {
            return new YamlParser();
        } else {
            throw new UnsupportedFileFormatException(fileFormat);
        }
    }
}
