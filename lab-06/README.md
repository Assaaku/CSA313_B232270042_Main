# Лаборатори №6 — Category-Partition ба PICT

## Оюутны мэдээлэл

- **Нэр:** Батбилэг Тэмүүлэн
- **Оюутны код:** B232270042
- **Хичээл:** F.CSA313 — Программ хангамжийн чанарын баталгаа ба тест

## 1. Зорилго

Энэ лабораторийн ажлаар тест тохиолдол сонгох хоёр аргыг ашигласан. Эхний хэсэгт банкны шилжүүлгийн функцийг Category-Partition аргаар задлан сонголт, төлөөлөх утга, хязгаарлалтууд болон тестийн спецификаци гаргасан. Хоёр дахь хэсэгт Microsoft PICT ашиглан pairwise covering array үүсгэж, гараар хийсэн дизайнтай харьцуулсан.

PICT-ийг Fedora Linux дээр эх кодоос build хийж ашигласан. PICT-ийн source болон build directory-г хичээлийн repository дотор хадгалаагүй.

---

# Хэсэг A — Category-Partition

## 2. Тестлэгдэх функц

```text
POST /transfer

{
  "from": ID,
  "to": ID,
  "amount": NUM,
  "currency": CODE
}
```

Үндсэн дүрмүүд:

- `from` данс системд байх ёстой.
- `from` данс frozen байж болохгүй.
- `to` данс системд байх ёстой.
- `from` болон `to` ижил данс байж болохгүй.
- `amount > 0`.
- Amount нь дансны үлдэгдлээс хэтэрч болохгүй.
- Өдрийн лимитээс хэтэрч болохгүй.
- Валют дансныхаас өөр бол ханшийн хөрвүүлэлт хийгдэнэ.

Амжилттай үед:

```text
201 {"result":"OK"}
```

Алдаатай үед:

```text
200 {"result":"ERROR_..."}
```

---

## 3. Сонголт ба төлөөлөх утгууд

| Category | Choice | Төлөөлөх утга |
|---|---|---|
| From | Valid | Данс байгаа, `frozen=false` |
| From | Frozen | Данс байгаа, `frozen=true` |
| From | Missing | From данс системд байхгүй |
| To | Valid | Өөр хүчинтэй данс |
| To | Missing | To данс системд байхгүй |
| To | SameAsFrom | `to == from` |
| Amount | Normal | 100 |
| Amount | ExactBalance | 1000 |
| Amount | NonPositive | 0 |
| Amount | OverBalance | 1500 |
| Currency | Same | MNT |
| Currency | Different | USD |
| DailyLimit | Enough | Үлдсэн лимит 2000 |
| DailyLimit | Insufficient | Үлдсэн лимит 50 |

`DailyLimit` нь request body дээр шууд байхгүй боловч шилжүүлгийн үр дүнд нөлөөлдөг учраас далд орчны сонголт гэж авч үзсэн.

---

## 4. ERROR / SINGLE / IF хязгаарлалтууд

| Category | Choice | Тэмдэглэгээ |
|---|---|---|
| From | Valid | — |
| From | Frozen | `[ERROR] [SINGLE]` |
| From | Missing | `[ERROR] [SINGLE]` |
| To | Valid | `[IF From=Valid]` |
| To | Missing | `[ERROR] [SINGLE] [IF From=Valid]` |
| To | SameAsFrom | `[ERROR] [SINGLE] [IF From=Valid]` |
| Amount | Normal | `[IF From=Valid AND To=Valid]` |
| Amount | ExactBalance | `[IF From=Valid AND To=Valid]` |
| Amount | NonPositive | `[ERROR] [SINGLE] [IF From=Valid AND To=Valid]` |
| Amount | OverBalance | `[ERROR] [SINGLE] [IF From=Valid AND To=Valid]` |
| Currency | Same | `[IF өмнөх сонголтууд хүчинтэй]` |
| Currency | Different | `[IF өмнөх сонголтууд хүчинтэй]` |
| DailyLimit | Enough | `[IF өмнөх сонголтууд хүчинтэй]` |
| DailyLimit | Insufficient | `[ERROR] [IF өмнөх сонголтууд хүчинтэй]` |

`[ERROR]` нь тухайн утга өөрөө алдааны нөхцөл үүсгэж байгааг тэмдэглэнэ. `[SINGLE]` нь тухайн алдаа expected result-ийг өөрөө тодорхойлж байгаа үед бусад доод түвшний сонголтуудтай бүх комбинацийг давтан үүсгэх шаардлагагүй гэж үзсэн. `[IF]` нь өмнөх шаардлагатай нөхцөлүүд хүчинтэй үед тухайн choice утгатай болохыг заана.

---

## 5. Бүрэн олонлог ба хязгаарлалтын дараах тоо

Хязгаарлалтгүйгээр:

```text
From       = 3
To         = 3
Amount     = 4
Currency   = 2
DailyLimit = 2
```

Тиймээс:

```text
3 × 3 × 4 × 2 × 2 = 144
```

Category-Partition-ийн `SINGLE` болон `IF` хязгаарлалтыг хэрэглэвэл:

```text
From ERROR            = 2 SINGLE
To ERROR              = 2 SINGLE
Amount ERROR          = 2 SINGLE
Хэвийн зам            = 2 amount × 2 currency × 2 limit
                       = 8

2 + 2 + 2 + 8 = 14
```

Иймээс Category-Partition түвшинд:

```text
144 → 14
```

Энэ `14` нь Category-Partition аргаар тооцсон боломжит test frame-ийн тоо юм. Харин дараагийн хэсэгт PICT pairwise алгоритм ашиглах тул PICT-ийн үүсгэсэн мөрийн тоо үүнтэй заавал тэнцүү байх шаардлагагүй.

---

## 6. Олон алдааны давуу эрэмбэ

Нэг хүсэлтэд хэд хэдэн алдаа зэрэг байгаа үед specification-д priority тодорхойлоогүй тул дараах дарааллыг сонгосон:

```text
From → To → Amount → DailyLimit
```

`from` данс өөрөө байхгүй эсвэл frozen байвал шилжүүлгийн дараагийн нөхцөлүүдийг шалгах утга багасна. Иймээс эхлээд source account, дараа нь destination account, дараа нь amount, эцэст нь өдрийн лимитийг шалгах нь логикийн хувьд ойлгомжтой гэж үзсэн.

---

## 7. Гараар гаргасан тестийн спецификаци

| № | From | To | Amount | Currency | DailyLimit | Хүлээгдэх үр дүн |
|---:|---|---|---|---|---|---|
| 1 | Valid | Valid | Normal | Same | Enough | `201 OK` |
| 2 | Valid | Valid | Normal | Different | Enough | `201 OK` |
| 3 | Valid | Valid | ExactBalance | Same | Enough | `201 OK` |
| 4 | Frozen | Valid | Normal | Same | Enough | `200 ERROR_FROZEN` |
| 5 | Missing | Valid | Normal | Same | Enough | `200 ERROR_NO_FROM` |
| 6 | Valid | Missing | Normal | Same | Enough | `200 ERROR_NO_TO` |
| 7 | Valid | SameAsFrom | Normal | Same | Enough | `200 ERROR_SAME_ACCOUNT` |
| 8 | Valid | Valid | NonPositive | Same | Enough | `200 ERROR_BAD_AMOUNT` |
| 9 | Valid | Valid | OverBalance | Same | Enough | `200 ERROR_INSUFFICIENT` |
| 10 | Valid | Valid | Normal | Same | Insufficient | `200 ERROR_LIMIT` |
| 11 | Frozen | Missing | OverBalance | Different | Insufficient | `200 ERROR_FROZEN` |
| 12 | Valid | Missing | OverBalance | Same | Insufficient | `200 ERROR_NO_TO` |

11-р мөрөнд хэд хэдэн алдаа зэрэг байгаа боловч сонгосон priority-ийн дагуу `From` эхэлж шалгагдах тул `ERROR_FROZEN` гэж үзсэн.

12-р мөрөнд `From` хүчинтэй боловч `To` байхгүй тул Amount болон DailyLimit-ийн алдаанаас өмнө `ERROR_NO_TO` буцна.

---

# Хэсэг B — PICT

## 8. PICT build

PICT-ийг Microsoft-ийн эх кодоос build хийсэн.

Ашигласан commit:

```text
pict commit ab76c25 2026-09-08
```

Build-ийн нотолгоо:

```text
results/pict-build.txt
```

PICT source нь:

```text
~/tools/pict
```

байршилд байгаа бөгөөд хичээлийн repository-д оруулаагүй.

---

## 9. Browser model — 2-way ба 3-way

Browser model:

```text
Content:   Allow, Restrict, Block
Cookies:   Allow, Restrict, Block
Popups:    Yes, No
Addons:    Yes, No
Attacks:   Yes, No
Forgeries: Yes, No
```

Үр дүн:

| Хамралт | Мөрийн тоо |
|---|---:|
| 2-way | **10** |
| 3-way | **20** |
| Бүрэн олонлог | **144** |

Лекц дээр pairwise хувилбарыг гараар 9 мөрөнд багтааж байсан бол PICT энэ моделд 10 мөр үүсгэсэн. PICT greedy алгоритм ашигладаг учраас бүх шаардлагатай хосыг хамруулах боловч заавал хамгийн бага боломжит мөрийн тоог олох баталгаагүй. Иймээс гараар 9 мөр гарсан байхад PICT 10 мөр гарч болох нь хэвийн.

3-way үед нэг дор гурван параметрийн combination-уудыг хамруулах шаардлагатай болсон тул мөрийн тоо 20 болж өссөн. Гэсэн хэдий ч энэ нь бүрэн олонлогийн 144 комбинациас нэлээд бага хэвээр байна.

---

## 10. Transfer model-уудын үр дүн

Гурван өөр model ажиллуулсан:

- `transfer-nc.model` — constraint байхгүй
- `transfer.model` — Category-Partition-ийн дагуу constraint-тай
- `transfer-neg.model` — ERROR утгуудыг `~` negative choice болгон тэмдэглэсэн

Үр дүн:

| Model | PICT мөр | Happy path мөр | Нэг мөр дэх хамгийн их алдааны тоо |
|---|---:|---:|---:|
| `transfer-nc.model` | **14** | **2** | **4** |
| `transfer.model` | **10** | **1** | **1** |
| `transfer-neg.model` | **19** | **4** | **1** |

### Хязгаарлалтгүй model

`transfer-nc.model` дээр error утгуудыг бусад энгийн утгатай адилхан авч үзсэн. Тиймээс PICT нэг мөрөнд хэд хэдэн алдааны нөхцөлийг зэрэг оруулсан.

Жишээ:

```text
Missing    Missing    NonPositive    Different    Insufficient
```

Энд `from` байхгүй, `to` байхгүй, amount буруу, daily limit хүрэлцэхгүй гэсэн олон алдаа нэг дор байна. Энэ тохиолдолд аль нэг эхний алдаа бусдыгаа нуух буюу **error masking** үүсэх боломжтой.

### Constraint-тай model

`transfer.model` дээр өндөр priority-тай error гарсан үед доод түвшний утгуудыг normal representative value болгож тогтоосон. Үүний үр дүнд 10 мөр гарсан бөгөөд нэг мөрөнд хамгийн ихдээ нэг үндсэн error condition үлдсэн.

### Negative model

`transfer-neg.model` дээр:

```text
~Frozen
~Missing
~SameAsFrom
~NonPositive
~OverBalance
~Insufficient
```

гэж ERROR утгуудыг negative choice болгосон.

PICT `~` negative утгуудыг нэг мөрөнд хамгийн ихдээ нэгийг оруулах тул `transfer-neg.model`-ийн бүх мөр хамгийн ихдээ нэг error condition-той болсон. Энэ нь Category-Partition-ийн `[ERROR]` санаатай ойролцоо ажиллаж байна.

`~` ашигласнаар **error masking-ийг зогсоохын тулд** A2 дээр ашигласан бүх constraint заавал хэрэгтэй биш болсон. Гэхдээ бодит domain дээр боломжгүй combination байгаа бол түүнийг хасах semantic constraint шаардлагатай хэвээр байж болно.

---

## 11. PICT гаралтаас бодит тест кейсүүд

PICT-ийн бодит output-оос дараах гурван мөрийг тест кейс болгон сонгов.

### Test case 1 — Happy path

`transfer-neg.model`-ийн бодит мөр:

```text
Valid    Valid    Normal    Same    Enough
```

#### Setup

- `ACC_FROM` данс байна.
- `ACC_FROM.frozen = false`
- `ACC_FROM.balance = 1000 MNT`
- `ACC_TO` гэсэн өөр хүчинтэй данс байна.
- Үлдсэн өдрийн лимит = `2000`
- Хоёр талын валют = `MNT`

#### Input

```json
{
  "from": "ACC_FROM",
  "to": "ACC_TO",
  "amount": 100,
  "currency": "MNT"
}
```

#### Oracle

```text
HTTP 201
{"result":"OK"}
```

---

### Test case 2 — Negative amount

`transfer-neg.model`-ийн бодит мөр:

```text
Valid    Valid    ~NonPositive    Same    Enough
```

#### Setup

- `ACC_FROM` хүчинтэй, frozen биш.
- Balance = `1000 MNT`
- `ACC_TO` хүчинтэй бөгөөд өөр данс.
- Өдрийн лимит хангалттай.

#### Input

```json
{
  "from": "ACC_FROM",
  "to": "ACC_TO",
  "amount": 0,
  "currency": "MNT"
}
```

#### Oracle

```text
HTTP 200
{"result":"ERROR_BAD_AMOUNT"}
```

Энэ нь `transfer-neg.model`-ийн нэг negative choice-той мөр бөгөөд бусад оролт хэвийн байна.

---

### Test case 3 — Олон алдаатай unconstrained мөр

`transfer-nc.model`-ийн бодит мөр:

```text
Valid    SameAsFrom    NonPositive    Same    Enough
```

#### Setup

- `ACC_FROM` хүчинтэй, frozen биш.
- Balance = `1000 MNT`.
- `from` болон `to` дээр ижил account ID ашиглана.
- Өдрийн лимит хангалттай.

#### Input

```json
{
  "from": "ACC_FROM",
  "to": "ACC_FROM",
  "amount": 0,
  "currency": "MNT"
}
```

Энд хоёр алдаа зэрэг байна:

1. `to == from`
2. `amount <= 0`

Өмнө тогтоосон:

```text
From → To → Amount → DailyLimit
```

priority-ийн дагуу `To` нөхцөл Amount-аас өмнө шалгагдана.

#### Oracle

```text
HTTP 200
{"result":"ERROR_SAME_ACCOUNT"}
```

Энэ жишээ нь constraint болон negative choice ашиглаагүй үед PICT олон error condition-ийг нэг мөрөнд оруулж болохыг харуулж байна.

---

## 12. Гараар хийсэн Category-Partition ба PICT-ийн харьцуулалт

Category-Partition нь input бүрийн утга болон алдааны семантикийг тодорхойлоход илүү ойлгомжтой байсан. Ялангуяа ERROR, SINGLE болон IF нөхцөлийг хүн өөрөө domain-ийн утгаар нь шийдэх боломжтой. Олон алдаа зэрэг үүсэх үед аль ERROR буцахыг тогтоохдоо мөн гараар хийсэн дизайн илүү тодорхой байсан.

PICT-ийн гол давуу тал нь олон параметртэй үед pairwise coverage-ийг автоматаар үүсгэдэг явдал байлаа. Гэхдээ PICT нь `Frozen`, `Missing`, `OverBalance` зэрэг утгуудын бизнес утгыг өөрөө мэдэхгүй. Тиймээс constraint эсвэл negative choice ашиглаагүй үед хэд хэдэн ERROR утгыг нэг мөрөнд оруулж болно.

Category-Partition нь **алдааны семантик** дээр илүү хүчтэй, харин PICT нь **комбинацийн хамралт** дээр илүү хүчтэй гэж үзсэн.

---

## 13. Дүгнэлт

Энэ лабораторид хамгийн их бодол шаардсан хэсэг нь параметрүүдийг зүгээр жагсаахаас илүү ямар choice нь ERROR, аль нь SINGLE байхыг шийдэх хэсэг байсан. Мөн request body дээр шууд байхгүй DailyLimit-ийг орчны далд сонголт гэж авч үзэх шаардлагатай болсон. Олон алдаа зэрэг байгаа үед specification priority тодорхойлоогүй тул `From → To → Amount → DailyLimit` гэсэн дарааллыг өөрөө тогтоосон. 

Category-Partition ашиглахад 144 боломжит combination-ийг 14 утгатай test frame болгон логикийн хувьд хязгаарлаж чадсан. PICT-ийн browser pairwise туршилт 10 мөр гаргасан нь лекцийн гараар хийсэн 9 мөрөөс нэгээр их байсан бөгөөд greedy алгоритм заавал minimum solution гаргадаггүйг харуулсан. Unconstrained transfer model дээр нэг мөрөнд дөрөв хүртэл error condition орсон нь error masking-ийн асуудлыг илүү ойлгомжтой харуулсан. Constraint ашиглахад нэг мөрөн дэх алдааны тоо нэг болсон бол `~` negative choice ашигласан model мөн нэг мөрөнд нэг negative condition хадгалсан.

 PICT нь pairwise combination-уудыг автоматаар хамруулахад тохиромжтой боловч бизнес дүрэм болон expected oracle-ийг өөрөө ойлгодоггүй. Иймээс бодит төсөл дээр эхлээд Category-Partition аргаар domain болон error semantics-ийг тодорхойлоод дараа нь түүнийг PICT-тэй хослуулж ашиглах нь илүү тохиромжтой гэж үзсэн.

---

## 14. AI Өнцөг

AI нь шууд шийдвэр гаргах багаж байх ёсгүй гэж би одоогийн байдлаар үзэж байна. Тиймээс тайлан засах, формат өөрчлөх, системийн талаар ерөнхий судалгаа хийх эхний ажилуудад маш түгээмэл хэрэглэж байгаа. Энэ нь миний бодлоор кодны аюулгүй байдалд маш чухал нөлөөтэй гэж бодож байна.