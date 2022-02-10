# jgettext
A Java-based gettext library

It currently supports the PO and JSON translation formats.

### API overview
- `TranslationParserFactory`
- `TranslationParser`
- `TranslationFile`
    - `TranslationEntry`: header
    - `TranslationEntry`: obsolete entries
    - `TranslationEntry`: entries
        - comments
        - msgctxt
        - obsolete lines
        - plural
        - fuzzy
        - `TranslationElement`: msgid
            - lines: text
            - tag: msgid
        - `TranslationElement`: msgstr
            - lines: text
            - tag: msgstr
        - `TranslationElement`: msgid_plural
        - `TranslationElement`: msgstr (plural)

### Dependencies
- jakarta.json-api
- jakarta.json



Copyright (C) 2020-2022 Javier Llorente javier@opensuse.org
