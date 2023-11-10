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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author Javier Llorente <javier@opensuse.org>
 */
public class YamlFile implements TranslationFile {
    
    private final List<TranslationEntry> entries;

    public YamlFile() {
        entries = new ArrayList<>();
    }

    @Override
    public List<TranslationEntry> getEntries() {
        return entries;
    }

    @Override
    public void addEntry(TranslationEntry entry) {
        entries.add(entry);
    }

    @Override
    public void updateEntry(int index, List<TranslationElement> elements) {
        entries.get(index).getMsgStrElement().set(elements.get(0).get());
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        StringBuilder space = new StringBuilder();        
        List<String> addedTags = new ArrayList<>();
        
        for (TranslationEntry entry : entries) {
            List<String> tags = Arrays.asList(entry.getMsgStrElement().getTag().split(":"));
            if (tags.size() == 1) {
                sb.append(entry.getMsgStrElement().toString()).append("\n");
            } else if (tags.size() > 1) {
                for (int i = 0; i < tags.size(); i++) {
                    if (i == 0) {
                        if (addedTags.isEmpty()) {
                            sb.append(tags.get(0)).append(":");
                            addedTags.add(tags.get(0));
                            sb.append("\n");
                        } else {
                            if (!addedTags.get(0).equals(tags.get(0))) {
                                sb.append(tags.get(0)).append(":");
                                addedTags.clear();
                                addedTags.add(tags.get(0));
                                sb.append("\n");
                            }
                        }
                    } else if (i != tags.size() - 1) {                        
                        if (i > addedTags.size() - 1) {
                            sb.append(space).append(tags.get(i)).append(":");
                            addedTags.add(tags.get(i)); 
                            sb.append("\n");
                        } else if (!addedTags.get(i).equals(tags.get(i))) {
                            sb.append(space).append(tags.get(i)).append(":");
                            addedTags.remove(i);
                            addedTags.add(tags.get(i));
                            sb.append("\n");
                        }
                    } else if (i == tags.size() - 1) {
                        sb.append(space).append(tags.get(i)).append(": ");
                    }                  
                    space.append("  ");
                }
                
                sb.append(String.join("\n", entry.getMsgStrElement().get()));
                sb.append("\n");
                space.setLength(0);
            }
        }
        return sb.toString();
    }
    
    
}
