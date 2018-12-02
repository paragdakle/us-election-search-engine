package core.utils;

public interface Constants {

    String HTML_SCRIPT_TAG_REGEX = "<script.*?>(.*?)</script>";

    String HTML_STYLE_TAG_REGEX = "<style.*?>(.*?)</style>";

    String HTML_COMMENT_REGEX = "<!--(.*?)-->";

    String HTML_TAG_REGEX = "<[/]?.*?>";

    String HTML_NBSP_REGEX = "nbsp";

    String SPECIAL_CHARACTER_REGEX = "[^a-zA-Z0-9 ]";

    String HYPHEN_REGEX = "-";

    String POSSESSIVE_REGEX = "'s";

    String NUMBER_REGEX = "[0-9]{5,}";

    String MULTISPACE_REGEX = "[ ]+";

    String CORPUS_DIR_PATH = "data/project/";

    String TOKENIZED_CORPUS_DIR_PATH = "output/corpus/";

    int MAX_BLOCK_FILES_COUNT = 3900;

    byte QE_ASSOCIATION = 1;

    byte QE_METRIC = 2;

    byte QE_SCALAR = 3;

    byte QE_ROCCHIO = 4;
}
