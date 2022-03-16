/*
 * Copyright (C) 2020-2021 Javier Llorente <javier@opensuse.org>
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
