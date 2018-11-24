package utils;

public interface Constants {

    String HTML_TAG_REGEX = "<[/]?.*?>";

    String SPECIAL_CHARACTER_REGEX = "[^a-zA-Z ]";

    String HYPHEN_REGEX = "-";

    String POSSESSIVE_REGEX = "'s";

    String NUMBER_REGEX = "[0-9]+";

    String MULTISPACE_REGEX = "[ ]+";

    String CORPUS_DIR_PATH = "data/all/";

    int MAX_BLOCK_FILES_COUNT = 1000;
}
