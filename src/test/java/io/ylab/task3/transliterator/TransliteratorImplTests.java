package io.ylab.task3.transliterator;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class TransliteratorImplTests {
  private static Transliterator transliterator;
  private static Transliterator transliteratorSwitch;

  @BeforeAll
  static void beforeAll() {
    transliterator = new TransliteratorImpl();
    transliteratorSwitch = new TransliteratorImplWithSwitch();
  }

  @ParameterizedTest
  @DisplayName("Map - Перевод транслитерация слов.")
  @CsvSource({
      "'HELLO! ПРИВЕТ! Go, boy!', 'HELLO! PRIVET! Go, boy!'",
      "'БОЛЬШИЕ & маленькие.', 'BOLSHIE & маленькие.'",
      "'АРБУЗ', 'ARBUZ'",
      "'БУЛКА', 'BULKA'",
      "'ВОЛК', 'VOLK'",
      "'ГРИБ', 'GRIB'",
      "'ДОМ', 'DOM'",
      "'ЕНИСЕЙ', 'ENISEI'",
      "'ЁЖ', 'EZH'",
      "'ЖУК', 'ZHUK'",
      "'ЗАЙКА', 'ZAIKA'",
      "'ИГЛА', 'IGLA'",
      "'ЙОД', 'IOD'",
      "'КОНЬ', 'KON'",
      "'ЛОМ', 'LOM'",
      "'МАМА', 'MAMA'",
      "'НОРА', 'NORA'",
      "'ОСА', 'OSA'",
      "'ПОБЕДА', 'POBEDA'",
      "'РОССИЯ', 'ROSSIIA'",
      "'СВЕТ', 'SVET'",
      "'ТУМАН', 'TUMAN'",
      "'УЛЫБКА', 'ULYBKA'",
      "'ФИЛИН', 'FILIN'",
      "'ХЛЕБ', 'KHLEB'",
      "'ЦАПЛЯ', 'TSAPLIA'",
      "'ЧЕЛОВЕК', 'CHELOVEK'",
      "'ШКОЛА', 'SHKOLA'",
      "'ЩУКА', 'SHCHUKA'",
      "'МЫСЛЬ', 'MYSL'",
      "'СОЛЬ', 'SOL'",
      "'ПОДЪЁМ', 'PODIEEM'",
      "'ЭРА', 'ERA'",
      "'ЮЛА', 'IULA'",
      "'ЯМАЛ', 'IAMAL'"
  })
  void transliterate(String source, String expect) {
    String result = transliterator.transliterate(source);
    Assertions.assertEquals(expect, result);
  }

  @ParameterizedTest
  @DisplayName("Switch - Перевод транслитерация слов.")
  @CsvSource({
      "'HELLO! ПРИВЕТ! Go, boy!', 'HELLO! PRIVET! Go, boy!'",
      "'БОЛЬШИЕ & маленькие.', 'BOLSHIE & маленькие.'",
      "'АРБУЗ', 'ARBUZ'",
      "'БУЛКА', 'BULKA'",
      "'ВОЛК', 'VOLK'",
      "'ГРИБ', 'GRIB'",
      "'ДОМ', 'DOM'",
      "'ЕНИСЕЙ', 'ENISEI'",
      "'ЁЖ', 'EZH'",
      "'ЖУК', 'ZHUK'",
      "'ЗАЙКА', 'ZAIKA'",
      "'ИГЛА', 'IGLA'",
      "'ЙОД', 'IOD'",
      "'КОНЬ', 'KON'",
      "'ЛОМ', 'LOM'",
      "'МАМА', 'MAMA'",
      "'НОРА', 'NORA'",
      "'ОСА', 'OSA'",
      "'ПОБЕДА', 'POBEDA'",
      "'РОССИЯ', 'ROSSIIA'",
      "'СВЕТ', 'SVET'",
      "'ТУМАН', 'TUMAN'",
      "'УЛЫБКА', 'ULYBKA'",
      "'ФИЛИН', 'FILIN'",
      "'ХЛЕБ', 'KHLEB'",
      "'ЦАПЛЯ', 'TSAPLIA'",
      "'ЧЕЛОВЕК', 'CHELOVEK'",
      "'ШКОЛА', 'SHKOLA'",
      "'ЩУКА', 'SHCHUKA'",
      "'МЫСЛЬ', 'MYSL'",
      "'СОЛЬ', 'SOL'",
      "'ПОДЪЁМ', 'PODIEEM'",
      "'ЭРА', 'ERA'",
      "'ЮЛА', 'IULA'",
      "'ЯМАЛ', 'IAMAL'"
  })
  void transliterateSwitch(String source, String expect) {
    String result = transliteratorSwitch.transliterate(source);
    Assertions.assertEquals(expect, result);
  }
}