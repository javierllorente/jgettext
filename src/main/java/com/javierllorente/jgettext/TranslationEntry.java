/*
 * Copyright (C) 2020, 2021 Javier Llorente <javier@opensuse.org>
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

import java.util.List;

/**
 *
 * @author javier
 */
public interface TranslationEntry {

    default List<String> getComments() {
        return null;
    }

    default void setComments(List<String> comments) {
    }

    default void addComment(String comment) {
    }

    default List<String> getMsgCtxt() {
        return null;
    }

    default void setMsgCtxt(List<String> msgCtxt) {
    }

    public TranslationElement getMsgIdElement();

    public void setMsgIdElement(TranslationElement element);

    public List<String> getMsgId();

    public void setMsgId(List<String> msgId);

    public void addMsgIdEntry(String msgIdEntry);
    
    default TranslationElement getMsgIdPluralElement() {
        return null;
    }

    default void setMsgIdPluralElement(TranslationElement msgIdPluralElement) {
    }

    default List<TranslationElement> getMsgStrElements() {
        return null;
    }

    default void setMsgStrElements(List<TranslationElement> elements) {
    }
    
    public TranslationElement getMsgStrElement();

    public void setMsgStrElement(TranslationElement element);

    public List<String> getMsgStr();

    public void setMsgStr(List<String> msgStr);

    public void addMsgStrEntry(String msgStrEntry);

    default List<String> getObsoleteEntries() {
        return null;
    }

    default void setObsoleteEntries(List<String> obsoleteEntries) {
    }

    default void addObsoleteEntry(String obsoleteEntry) {
    }

    default boolean isPlural() {
        return false;
    }

    default void setPlural(boolean plural) {
    }
    
    default boolean isFuzzy() {
        return false;
    }
    
    default void setFuzzy(boolean fuzzy) {
    }
    
    default void addFuzzyFlag() {
    }
    
    default void removeFuzzyFlag() {
    }
}
