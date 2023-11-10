/*
 * Copyright (C) 2023 Javier Llorente <javier@opensuse.org>
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
 * @author Javier Llorente <javier@opensuse.org>
 */
public class YamlEntry implements TranslationEntry {
    
    private TranslationElement msgIdElement;
    private TranslationElement msgStrElement;

    public YamlEntry() {
         msgIdElement = new YamlElement();
         msgStrElement = new YamlElement();
    }

    @Override
    public TranslationElement getMsgIdElement() {
        return msgIdElement;
    }

    @Override
    public void setMsgIdElement(TranslationElement msgIdlement) {
        this.msgIdElement = msgIdlement;
    }

    @Override
    public List<String> getMsgId() {
        return msgIdElement.get();
    }

    @Override
    public void setMsgId(List<String> msgId) {
        msgIdElement.set(msgId);
    }

    @Override
    public void addMsgIdEntry(String msgIdEntry) {
        msgIdElement.add(msgIdEntry);
    }

    @Override
    public TranslationElement getMsgStrElement() {
        return msgStrElement;
    }

    @Override
    public void setMsgStrElement(TranslationElement msgStrElement) {
        this.msgStrElement = msgStrElement;
    }

    @Override
    public List<String> getMsgStr() {
        return msgStrElement.get();
    }

    @Override
    public void setMsgStr(List<String> msgStr) {
        msgStrElement.set(msgStr);
    }

    @Override
    public void addMsgStrEntry(String msgStrEntry) {
        msgStrElement.add(msgStrEntry);
    }
    
}
