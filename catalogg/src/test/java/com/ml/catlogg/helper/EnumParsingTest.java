package com.ml.catlogg.helper;

import com.ml.catalogg.enums.FormatEnum;
import com.ml.catalogg.enums.TypeEnum;
import com.ml.catalogg.helper.FormatEnumParser;
import com.ml.catalogg.helper.TypeEnumParser;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class EnumParsingTest {
    @Test
    public void runAll() {
        givenFormatAsString_whenParse_thenReturnEnum();
        givenTypeAsString_whenParse_thenReturnEnum();
    }
    @Test
    public void givenFormatAsString_whenParse_thenReturnEnum() {
        testParsingFormatContainingCd();
        testParsingFormatContainingSacd();
        testParsingFormatContainingCassette();
        testParsingFormatContainingShellac();
        testParsingFormatContainingVinyl();
        testParsingFormatNotSupported();
    }
    @Test
    public void givenTypeAsString_whenParse_thenReturnEnum() {
        testParsingTypeContainingALbum();
        testParsingTypeContainingDemo();
        testParsingTypeContainingEp();
        testParsingTypeContainingCompilation();
        testParsingTypeContainingLive();
        testParsingTypeContainingMixtape();
        testParsingTypeContainingSingle();
        testParsingTypeContainingSoundtrack();
        testParsingTypeNoSupported();
    }
    @Test
    public void testParsingFormatContainingCd() {
        String[] testedValues = {"CD", "cd", "CD-R", "HDCD", "SACD"};
        FormatEnum expectedResult = FormatEnum.CD;
        Assertions.assertAll("parsed to CD",
                () -> Assertions.assertEquals(FormatEnumParser.parse(testedValues[0]), expectedResult),
                () -> Assertions.assertEquals(FormatEnumParser.parse(testedValues[1]), expectedResult),
                () -> Assertions.assertEquals(FormatEnumParser.parse(testedValues[2]), expectedResult),
                () -> Assertions.assertEquals(FormatEnumParser.parse(testedValues[3]), expectedResult),
                () -> Assertions.assertNotEquals(FormatEnumParser.parse(testedValues[4]), expectedResult)
        );
    }
    @Test
    public void testParsingFormatContainingVinyl() {
        String[] testedValues = {"Vinyl", "7\" Vinyl", "7\" Flexi-disc", "Flexi-disc"};
        FormatEnum expectedResult = FormatEnum.VINYL;
        Assertions.assertAll("parsed to VINYL",
                () -> Assertions.assertEquals(FormatEnumParser.parse(testedValues[0]), expectedResult),
                () -> Assertions.assertEquals(FormatEnumParser.parse(testedValues[1]), expectedResult),
                () -> Assertions.assertEquals(FormatEnumParser.parse(testedValues[2]), expectedResult),
                () -> Assertions.assertEquals(FormatEnumParser.parse(testedValues[3]), expectedResult)
        );
    }
    @Test
    public void testParsingFormatContainingCassette() {
        String[] testedValues = {"Cassette", "Microcassette"};
        FormatEnum expectedResult = FormatEnum.CASSETTE;
        Assertions.assertAll("parsed to CASSETTE",
                () -> Assertions.assertEquals(FormatEnumParser.parse(testedValues[0]), expectedResult),
                () -> Assertions.assertEquals(FormatEnumParser.parse(testedValues[1]), expectedResult)
        );
    }
    @Test
    public void testParsingFormatContainingShellac() {
        String[] testedValues = {"Shellac", "10\" Shellac"};
        FormatEnum expectedResult = FormatEnum.SHELLAC;
        Assertions.assertAll("parsed to SHELLAC",
                () -> Assertions.assertEquals(FormatEnumParser.parse(testedValues[0]), expectedResult),
                () -> Assertions.assertEquals(FormatEnumParser.parse(testedValues[1]), expectedResult)
        );
    }
    @Test
    public void testParsingFormatContainingSacd() {
        String[] testedValues = {"SACD", "Hybrid SACD", "Blu-spec CD", "SHM-CD", "CD"};
        FormatEnum expectedResult = FormatEnum.SACD;
        Assertions.assertAll("parsed to SACD",
                () -> Assertions.assertEquals(FormatEnumParser.parse(testedValues[0]), expectedResult),
                () -> Assertions.assertEquals(FormatEnumParser.parse(testedValues[1]), expectedResult),
                () -> Assertions.assertEquals(FormatEnumParser.parse(testedValues[2]), expectedResult),
                () -> Assertions.assertEquals(FormatEnumParser.parse(testedValues[3]), expectedResult),
                () -> Assertions.assertNotEquals(FormatEnumParser.parse(testedValues[4]), expectedResult)
        );
    }
    @Test
    public void testParsingFormatNotSupported() {
        String[] testedValues = {"DVD", "HD-DVD", "Cartridge", "LaserDisc", "CD", "", null};
        FormatEnum expectedResult = FormatEnum.OTHER;
        Assertions.assertAll("parsed to OTHER",
                () -> Assertions.assertEquals(FormatEnumParser.parse(testedValues[0]), expectedResult),
                () -> Assertions.assertEquals(FormatEnumParser.parse(testedValues[1]), expectedResult),
                () -> Assertions.assertEquals(FormatEnumParser.parse(testedValues[2]), expectedResult),
                () -> Assertions.assertEquals(FormatEnumParser.parse(testedValues[3]), expectedResult),
                () -> Assertions.assertNotEquals(FormatEnumParser.parse(testedValues[4]), expectedResult),
                () -> Assertions.assertEquals(FormatEnumParser.parse(testedValues[5]), expectedResult),
                () -> Assertions.assertEquals(FormatEnumParser.parse(testedValues[6]), expectedResult)
        );
    }
    @Test
    public void testParsingTypeContainingALbum() {
        String[] testedValues = {"Album", "Compilation"};
        TypeEnum expectedResult = TypeEnum.ALBUM;
        Assertions.assertAll("parsed to ALBUM",
                () -> Assertions.assertEquals(TypeEnumParser.parse(testedValues[0]), expectedResult),
                () -> Assertions.assertNotEquals(TypeEnumParser.parse(testedValues[1]), expectedResult)
        );
    }
    @Test
    public void testParsingTypeContainingEp() {
        String[] testedValues = {"EP", "Audiobook"};
        TypeEnum expectedResult = TypeEnum.EP;
        Assertions.assertAll("parsed to EP",
                () -> Assertions.assertEquals(TypeEnumParser.parse(testedValues[0]), expectedResult),
                () -> Assertions.assertNotEquals(TypeEnumParser.parse(testedValues[1]), expectedResult)
        );
    }
    @Test
    public void testParsingTypeContainingSingle() {
        String[] testedValues = {"Single", "Soundtrack"};
        TypeEnum expectedResult = TypeEnum.SINGLE;
        Assertions.assertAll("parsed to SINGLE",
                () -> Assertions.assertEquals(TypeEnumParser.parse(testedValues[0]), expectedResult),
                () -> Assertions.assertNotEquals(TypeEnumParser.parse(testedValues[1]), expectedResult)
        );
    }
    @Test
    public void testParsingTypeContainingDemo() {
        String[] testedValues = {"Demo", "Album"};
        TypeEnum expectedResult = TypeEnum.DEMO;
        Assertions.assertAll("parsed to DEMO",
                () -> Assertions.assertEquals(TypeEnumParser.parse(testedValues[0]), expectedResult),
                () -> Assertions.assertNotEquals(TypeEnumParser.parse(testedValues[1]), expectedResult)
        );
    }
    @Test
    public void testParsingTypeContainingMixtape() {
        String[] testedValues = {"Mixtape", "EP"};
        TypeEnum expectedResult = TypeEnum.MIXTAPE;
        Assertions.assertAll("parsed to MIXTAPE",
                () -> Assertions.assertEquals(TypeEnumParser.parse(testedValues[0]), expectedResult),
                () -> Assertions.assertNotEquals(TypeEnumParser.parse(testedValues[1]), expectedResult)
        );
    }
    @Test
    public void testParsingTypeContainingCompilation() {
        String[] testedValues = {"Compilation", "Album"};
        TypeEnum expectedResult = TypeEnum.COMPILATION;
        Assertions.assertAll("parsed to COMPILATION",
                () -> Assertions.assertEquals(TypeEnumParser.parse(testedValues[0]), expectedResult),
                () -> Assertions.assertNotEquals(TypeEnumParser.parse(testedValues[1]), expectedResult)
        );
    }
    @Test
    public void testParsingTypeContainingSoundtrack() {
        String[] testedValues = {"Soundtrack", "Interview"};
        TypeEnum expectedResult = TypeEnum.SOUNDTRACK;
        Assertions.assertAll("parsed to SOUNDTRACK",
                () -> Assertions.assertEquals(TypeEnumParser.parse(testedValues[0]), expectedResult),
                () -> Assertions.assertNotEquals(TypeEnumParser.parse(testedValues[1]), expectedResult)
        );
    }
    @Test
    public void testParsingTypeContainingLive() {
        String[] testedValues = {"Live", "Album"};
        TypeEnum expectedResult = TypeEnum.LIVE;
        Assertions.assertAll("parsed to LIVE",
                () -> Assertions.assertEquals(TypeEnumParser.parse(testedValues[0]), expectedResult),
                () -> Assertions.assertNotEquals(TypeEnumParser.parse(testedValues[1]), expectedResult)
        );
    }
    @Test
    public void testParsingTypeNoSupported() {
        String[] testedValues = {"", null, "DJ-mix", "Broadcast", "Field recording"};
        TypeEnum expectedResult = TypeEnum.OTHER;
        Assertions.assertAll("parsed to OTHER",
                () -> Assertions.assertEquals(TypeEnumParser.parse(testedValues[0]), expectedResult),
                () -> Assertions.assertEquals(TypeEnumParser.parse(testedValues[1]), expectedResult),
                () -> Assertions.assertEquals(TypeEnumParser.parse(testedValues[2]), expectedResult),
                () -> Assertions.assertEquals(TypeEnumParser.parse(testedValues[3]), expectedResult),
                () -> Assertions.assertEquals(TypeEnumParser.parse(testedValues[4]), expectedResult)
        );
    }
}
