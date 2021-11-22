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

import java.util.List;

/**
 *
 * @author javier
 */
public class JsonEntry implements TranslationEntry {
    
    private TranslationElement msgIdElement;
    private TranslationElement msgStrElement;

    public JsonEntry() {
        msgIdElement = new JsonElement();
        msgStrElement = new JsonElement();
    }

    @Override
    public TranslationElement getMsgIdElement() {
        return msgIdElement;
    }

    @Override
    public void setMsgIdElement(TranslationElement msgIdElement) {
        this.msgIdElement = msgIdElement;
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

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        if (msgIdElement != null) {
            sb.append(msgIdElement.toString());
        }

        if (msgStrElement != null) {
            sb.append(msgStrElement.toString());
        }

        return sb.toString();
    }
    
}
