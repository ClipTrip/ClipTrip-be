package com.cliptripbe.global.util.prompt.type;

import com.cliptripbe.feature.user.domain.type.Language;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum LanguagePromptType {

    ENGLISH(
        // Single Place Prompt
        """
            You are a translation assistant. Please translate the following place information from Korean to English.
                    
            Place Name: %s
            Road Address: %s
                    
            Return ONLY in JSON format like:
            {
              "translatedName": "...",
              "translatedRoadAddress": "..."
            }
            """,
        // Batch Place Prompt
        """
            You are a professional translator. Translate the provided list of places from Korean to English. 
            Your sole mission is to provide a perfectly translated JSON array that contains only English characters, numerals, and allowed symbols.
            The input is a list of JSON objects, and the output must be a single JSON array containing objects with 'index', 'translatedName' and 'translatedRoadAddress' fields. 
            Ensure all string values are enclosed in double quotes. 
            **The final response must not contain any characters other than English, numerals, and allowed symbols.** Respond ONLY with the JSON array, with no additional text or explanations. For example: 
            [{"index": 1, "translatedName": "Name", "translatedRoadAddress": "Address"}].
                    
            %s
            """
    ),

    JAPANESE(
        // Single Place Prompt
        """
            You are a translation assistant. Please translate the following place information from Korean to Japanese.
                    
            Place Name: %s
            Road Address: %s
                    
            Return ONLY in JSON format like:
            {
              "translatedName": "...",
              "translatedRoadAddress": "..."
            }
            """,
        // Batch Place Prompt
        """
            You are a professional translator specializing in Korean to Japanese place names, road addresses, and trade names.
            Your sole mission is to provide a perfectly translated JSON array that contains only Japanese characters (including Katakana and Kanji), numerals, and allowed symbols.
                    
            Translation Rules:
            - For proper nouns or words without a standard Japanese translation, you must translate them using Katakana based on their Korean pronunciation.
            - You must translate all Korean and English text into Japanese.
            - You must preserve numbers, hyphens (-), slashes (/), commas (,), and spaces.
                    
            The input is a list of JSON objects, and the output must be a single JSON array containing objects with 'index', 'translatedName', and 'translatedRoadAddress' fields.
            **The final response must not contain any characters other than Japanese, numerals, and allowed symbols.** Respond ONLY with the JSON array.
                    
            %s
            """
    ),

    CHINESE_SIMPLIFIED(
        // Single Place Prompt
        """
            You are a translation assistant. Please translate the following place information from Korean to Simplified Chinese.
                    
            Place Name: %s
            Road Address: %s
                    
            Return ONLY in JSON format like:
            {
              "translatedName": "...",
              "translatedRoadAddress": "..."
            }
            """,
        // Batch Place Prompt
        """
            You are a professional translator specializing in Korean to Simplified Chinese place names, road addresses, and trade names.
            Your sole mission is to provide a perfectly translated JSON array that contains only Simplified Chinese characters, numerals, and allowed symbols.
                    
            Translation Rules:
            - For proper nouns or names without a standard Chinese translation, you must use a phonetic translation (transliteration).
            - You must translate all Korean and English text into Chinese.
            - You must preserve numbers, hyphens (-), slashes (/), commas (,), and spaces.
            - For famous places, prioritize official Chinese names. For brand names, use widely accepted translations.
                    
            The input is a list of JSON objects, and the output must be a single JSON array containing objects with 'index', 'translatedName', and 'translatedRoadAddress' fields.
            **The final response must not contain any characters other than Simplified Chinese, numerals, and allowed symbols.** Respond ONLY with the JSON array, with no additional text or explanations.
                    
            %s
            """
    );
    // (Note: KOREAN prompt is removed as per your request)

    private final String singlePlacePrompt;
    private final String batchPrompt;

    public static LanguagePromptType findByLanguage(Language language) {
        return switch (language) {
            case JAPANESE -> JAPANESE;
            case CHINESE -> CHINESE_SIMPLIFIED;
            case ENGLISH -> ENGLISH;
            default -> null; // Returns null for KOREAN or unsupported languages
        };
    }
}