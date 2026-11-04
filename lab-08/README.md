# Лаборатори №8 — Mockito mock объектууд

## Оюутны мэдээлэл

- **Нэр:** Батбилэг Тэмүүлэн
- **Оюутны код:** B232270042
- **Хичээл:** F.CSA313 — Программ хангамжийн чанарын баталгаа ба тест

## Орчин

### Java

```text
openjdk version "21.0.12.1" 2026-08-18
OpenJDK Runtime Environment (Red_Hat-21.0.12.1.1-1) (build 21.0.12.1+1)
OpenJDK 64-Bit Server VM (Red_Hat-21.0.12.1.1-1) (build 21.0.12.1+1, mixed mode, sharing)
```

### Maven

```text
Apache Maven 3.9.11 (Red Hat 3.9.11-5)
Maven home: /usr/share/maven
Java version: 21.0.8, vendor: JetBrains s.r.o., runtime: /home/asaku/android-studio/jbr
Default locale: en_US, platform encoding: UTF-8
OS name: "linux", version: "7.2.9-100.fc43.x86_64", arch: "amd64", family: "unix"
```

## Зорилго

Энэ лабораторийн ажлаар `CourseRegistrationService` классын `StudentRepository` болон `EmailSender` хамаарлуудыг Mockito mock объектуудаар орлуулж бизнес логикийг тусгаарлагдсан unit test-ээр шалгав.

Энэхүү лабораторийн ажлыг хугацаанаас хоцорч гүйцэтгэсэн боловч дараагийн лабораториудын суурь ойлголт болон Mockito ашиглалтын дадалд хэрэгтэй гэж үзсэн тул бүрэн хийж дуусгалаа. Хоцорсон явдалд хүлцэл өчье. Ядаж “хоцорсон ч хийсэн” гэдэг ангилалд үлдэхийг зорив.

## Хэсэг A — Mockito тестүүд

Дараах Mockito болон JUnit боломжуудыг ашигласан:

- `@Mock`
- `when(...).thenReturn(...)`
- `verify(...)`
- `never()`
- `verifyNoInteractions(...)`
- `doThrow(...)`
- `assertThrows(...)`
- `ArgumentCaptor`
- `@ParameterizedTest`
- `@CsvSource`

Нийт **10 тест** ажилласан. A1-A7 нь 7 тест бөгөөд A8-ийн `@CsvSource` дэх 3 мөр тус бүр тусдаа тест болж тоологдсон.

Эцсийн үр дүн:

```text
Tests run: 10, Failures: 0, Errors: 0, Skipped: 0
BUILD SUCCESS
```

Бүтэн Maven гаралт:

```text
results/mvn-test.txt
```

### A8 дахь lenient() тайлбар

A8 параметржүүлсэн тестийн мөр бүр өөр execution path болон өөр stub ашигладаг тул тухайн мөрөнд зарим stub ашиглагдахгүй байж болно. Mockito-ийн strict stubs горим үүнийг unnecessary stubbing гэж үзэж болох учраас зөвхөн эдгээр scenario-specific stub дээр `lenient().when(...)` ашигласан.

## Хэсэг B — Мини мутаци

Тестүүд бодит алдааг барьж байгаа эсэхийг шалгахын тулд `CourseRegistrationService` кодод гурван түр өөрчлөлт хийж тестүүдийг дахин ажиллуулсан. Мутаци бүрийн дараа эх кодыг сэргээсэн.

| Мутант | Түр өөрчлөлт | Унасан тестүүд | Үр дүн |
|---|---|---|---|
| Mutant 1 | `if (!student.active())` шалгалтыг устгасан | `идэвхгүйОюутанБол_ERROR_INACTIVE_STUDENTБуцаажИмэйлтэйХарьцахгүй`; A8-ийн `INACTIVE` мөр | 2 Failures, BUILD FAILURE |
| Mutant 2 | `saveRegistration(...)` дуудалтыг устгасан | `идэвхтэйОюутан_шаардлагаХангасанБол_OKБуцаажХадгалжИмэйлИлгээнэ`; `имэйлExceptionШидсэнЧ_OKБуцаажБүртгэлийгХадгална` | 2 Failures, BUILD FAILURE |
| Mutant 3 | `courseId == null` шалгалтыг устгасан | `nullОролтБол_IllegalArgumentExceptionШидэжХамаарлуудтайХарьцахгүй` | 1 Failure, BUILD FAILURE |

Мутацийн бүтэн гаралтууд:

- `results/mvn-test-mutant-1.txt`
- `results/mvn-test-mutant-2.txt`
- `results/mvn-test-mutant-3.txt`

Mutant 3-ыг A6 тест барьсан тул `studentId` болон `courseId` хоёр аргументын null нөхцөлийг тус тус шалгаж байгаа тест хангалттай байв.

## Дүгнэлт

Mockito mock нь хамаарлын behavior-ийг тест дотроос тохируулж, дуудалтуудыг verify хийх боломж олгодгоор гараар бичсэн fake implementation-оос ялгаатай. Өгөгдлийн сан болон и-мэйл үйлчилгээ шиг unit test-ийн гаднах хамаарлуудыг mock хийх нь тохиромжтой. Харин энгийн value object эсвэл тестэлж байгаа үндсэн бизнес логикийг өөрийг нь mock хийх шаардлагагүй. 

`verify` ашигласнаар зөв үр дүнгээс гадна шаардлагатай dependency interaction үнэхээр болсон эсэхийг шалгаж чадсан. Гэхдээ хэт олон implementation detail-ийг verify хийвэл refactor хийх бүрд behavior өөрчлөгдөөгүй байсан ч тест эвдэрдэг хэврэг бүтэц үүсч болно.

 Гурван mutation бүгд дор хаяж нэг тестийг унагасан нь тестүүд зөвхөн ногоон харагдахаас гадна бодит логикийн өөрчлөлтийг барьж байгааг харуулсан. Эцсийн эх кодыг сэргээсний дараа бүх 10 тест дахин амжилттай ажилласан.
