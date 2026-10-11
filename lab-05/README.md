# Лаборатори №5 — API систем тест: Postman ба Newman

## Оюутны мэдээлэл

- **Нэр:** Батбилэг Тэмүүлэн
- **Оюутны код:** B232270042
- **Хичээл:** F.CSA313 — Программ хангамжийн чанарын баталгаа ба тест

---

## 1. Зорилго

Энэ лабораторийн ажлаар REST API дээр системийн түвшний тест зохиож, Postman болон Newman ашиглан тестүүдийг автоматжуулж ажиллуулсан. Тестийн дизайны үед тестлэгдэх функцийг тодорхойлох, сонголтууд гаргах, төлөөлөх утгууд сонгох, спецификаци байгуулах, бодит тест кейс болгон хэрэгжүүлэх гэсэн дарааллыг ашигласан.

Энэ удаа Postman collection-ийг бүхэлд нь UI дээр гараар байгуулахын оронд JSON collection-ийг кодоор үүсгэж, дараа нь Postman-д import хийж шалгасан. Миний хувьд тогтмол ажиллуулах тестүүд дээр ийм арга илүү тохиромжтой санагдсан. Collection нь энгийн файл хэлбэрээр repository-д хадгалагдах учраас Git ашиглан өөрчлөлтийг хянах, өөр машин эсвэл орчинд ижил тестийг дахин ажиллуулах, багийн бусад гишүүдтэй хуваалцахад хялбар байдаг. Мөн Newman нь CLI хэрэгсэл учраас shell script, CI/CD pipeline болон шаардлагатай үед Ansible зэрэг автоматжуулалтын workflow-с ажиллуулах боломжтой. Ингэснээр хөгжүүлэгч болон QA инженерүүдэд ижил тестийн багцыг давтан, тогтвортой ажиллуулах ажлыг хялбарчлах боломжтой. Харин Postman UI нь request, response болон test script-үүдийг харах, турших, debug хийхэд ашиглахад тохиромжтой хэвээр байна. Би одоогийн байдлаар автоматжуулалт болон kubernetes талд илүү суурилж ажиллаж байгаа тул энэ өнцөгөөс илүү онцлог байдлаар ажиллах гэж үзлээ.

---

## 2. Ашигласан орчин

| Хэрэгсэл | Хувилбар |
|---|---|
| Node.js | `v22.22.2` |
| Newman | `6.2.2` |
| Postman | Desktop application |
| Үйлдлийн систем | Fedora Linux |

Тестийн локал API:

```text
http://localhost:3000
```

Collection variable:

```text
baseUrl = http://localhost:3000
```

Үндсэн тестлэгдсэн функц:

```text
POST /registrations
```

---

## 3. Сонголт ба төлөөлөх утгууд

| Сонголт | Эквивалент анги | Төлөөлөх утга |
|---|---|---|
| `studentID` төлөв | Идэвхтэй оюутан | `status = "active"` |
| `studentID` төлөв | Идэвхгүй оюутан | `status = "inactive"` |
| `studentID` төлөв | Оюутан байхгүй | `L5_DOES_NOT_EXIST` |
| `coursesTaken` | Урьдач нөхцөл хангана | `["CS201"]` |
| `coursesTaken` | Урьдач нөхцөл хангахгүй | `[]` |
| `courseID` төлөв | Хичээл байгаа | `L5_HAPPY_COURSE` |
| `courseID` төлөв | Хичээл байхгүй | `L5_COURSE_DOES_NOT_EXIST` |
| `prerequisites` | Бүгдийг үзсэн | `["CS201"]`, coursesTaken=`["CS201"]` |
| `prerequisites` | Огт үзээгүй | `["CS201"]`, coursesTaken=`[]` |
| `prerequisites` | Заримыг үзсэн | Жишээ нь `["CS201","CS202"]`, coursesTaken=`["CS201"]` |
| `prerequisites` | Урьдач нөхцөлгүй | `[]` |
| Request body | Шаардлагатай талбар дутуу | `courseID` байхгүй |

---

## 4. Тестийн спецификаци

| № | Тестийн нөхцөл | Setup / өгөгдөл | HTTP status | Хүлээгдэх `result` |
|---:|---|---|---:|---|
| 1 | Happy path | Идэвхтэй оюутан, `CS201` үзсэн, course байгаа | 201 | `OK` |
| 2 | Оюутан байхгүй | Course байгаа боловч `studentID` системд байхгүй | 200 | `ERROR_NO_STUDENT` |
| 3 | Оюутан идэвхгүй | `status = "inactive"` | 200 | `ERROR_INACTIVE_STUDENT` |
| 4 | Хичээл байхгүй | Идэвхтэй оюутан байгаа боловч `courseID` байхгүй | 200 | `ERROR_NO_COURSE` |
| 5 | Урьдач нөхцөл дутуу | `coursesTaken=[]`, course нь `CS201` шаарддаг | 200 | `ERROR_PREREQUISITES` |
| 6 | Урьдач нөхцөлгүй хичээл | Идэвхтэй оюутан, `prerequisites=[]` | 201 | `OK` |
| 7 | Шаардлагатай талбар дутуу | `courseID` илгээгээгүй | 400 | `ERROR_BAD_REQUEST` |
| 8 | Давхар алдаа | Оюутан inactive бөгөөд course байхгүй | 200 | `ERROR_INACTIVE_STUDENT` |

---

## 5. Давхар алдааны туршилт

Нэг хүсэлтэд хоёр алдаа зэрэг үүсэх үед аль алдаа түрүүлж буцах нь урьдчилан тодорхойгүй байсан.

Туршилтад:

- оюутны `status = inactive`
- `courseID` системд байхгүй

гэсэн хоёр нөхцөлийг зэрэг үүсгэсэн.

Серверийн бодит хариу:

```json
{
  "result": "ERROR_INACTIVE_STUDENT"
}
```

байсан.

Иймээс одоогийн хэрэгжилт дээр inactive student шалгалт нь course байхгүй эсэхийн шалгалтаас өмнө хийгдэж байгааг тестээр тогтоосон.

---

## 6. Collection-ийн бүтэц

Collection дотор нийт 8 үндсэн scenario үүсгэсэн.

```text
Lab05 API System Test
├── 01 - Happy Path
├── 02 - Student Does Not Exist
├── 03 - Inactive Student
├── 04 - Course Does Not Exist
├── 05 - Missing Prerequisite
├── 06 - No Prerequisites Boundary
├── 07 - Missing Required Field
└── 08 - Double Error
```

Scenario бүр шаардлагатай өгөгдлөө өөрийн `PUT` setup хүсэлтээр үүсгээд дараа нь `POST /registrations` хүсэлтийг ажиллуулдаг. Иймээс нэг scenario өмнөх scenario-гийн setup-аас шууд хамаарахгүй.

Бүртгэл амжилттай үед `registrationID`-ийн яг утгыг шалгаагүй. Сервер ажиллах хугацаанд энэ утга нэмэгддэг учраас яг `1`, `2` гэх мэт утга шаардах нь тестийг тогтворгүй болгох боломжтой.

Registration request бүр дээр үндсэн хоёр oracle ашигласан:

1. HTTP status зөв эсэх
2. Response JSON-ийн `result` зөв эсэх

---

## 7. PASS ажиллуулалт

Үндсэн collection:

```text
lab05-collection.json
```

Ажиллуулсан команд:

```bash
newman run lab05-collection.json
```

Бүтэн гаралт:

```text
results/newman-pass.txt
```

### Үр дүн

| Үзүүлэлт | Executed | Failed |
|---|---:|---:|
| Iterations | 1 | 0 |
| Requests | 20 | 0 |
| Test scripts | 8 | 0 |
| Assertions | **16** | **0** |

Exit code:

```text
0
```

Үндсэн collection-ийн 8 scenario болон 16 assertion бүгд амжилттай ажилласан.

---

## 8. Зориудын FAIL ажиллуулалт

Fail туршилтад үндсэн collection-ийг өөрчлөхгүйгээр тусдаа файл ашигласан:

```text
lab05-collection-fail.json
```

Happy path дээр серверийн зөв response status нь:

```text
201 Created
```

байхад fail collection дээр зориудаар:

```javascript
pm.response.to.have.status(200);
```

гэж буруу oracle өгсөн.

Ажиллуулсан команд:

```bash
newman run lab05-collection-fail.json
```

Бүтэн гаралт:

```text
results/newman-fail.txt
```

### Үр дүн

| Үзүүлэлт | Executed | Failed |
|---|---:|---:|
| Iterations | 1 | 0 |
| Requests | 20 | 0 |
| Test scripts | 8 | 0 |
| Assertions | **16** | **1** |

Exit code:

```text
1
```

Fail болсон assertion:

```text
Status 201
expected response to have status code 200 but got 201
```

Энэ туршилтаар API өөрөө зөв ажиллаж байсан боловч oracle зориудаар буруу байхад Newman тестийн багцыг FAIL болгож, `exit code 1` буцааж байгааг шалгасан.

---

## 9. Сервер DOWN ажиллуулалт

Серверийг `Ctrl+C` ашиглан бүрэн зогсоосны дараа үндсэн collection-ийг дахин ажиллуулсан.

Ажиллуулсан команд:

```bash
newman run lab05-collection.json
```

Бүтэн гаралт:

```text
results/newman-down.txt
```

Request-үүд дээр дараах connection error гарсан:

```text
connect ECONNREFUSED 127.0.0.1:3000
```

### Үр дүн

| Үзүүлэлт | Executed | Failed |
|---|---:|---:|
| Iterations | 1 | 0 |
| Requests | **20** | **20** |
| Test scripts | 8 | 0 |
| Assertions | **16** | **16** |

Exit code:

```text
1
```

Энэ тохиолдол нь FAIL collection-ийн oracle алдаанаас өөр төрлийн алдаа юм. FAIL туршилтын үед сервер ажиллаж байсан бөгөөд зөв response ирсэн ч тестийн хүлээгдэж буй утгыг зориудаар буруу өгсөн. DOWN туршилтын үед сервертэй холболт огт үүсээгүй учраас response авах боломжгүй байсан. Иймээс энэ нь **interface / connection failure**, харин өмнөх нь **oracle / assertion failure** юм.

---

## 10. PASS, FAIL, DOWN харьцуулалт

| Туршилт | Серверийн төлөв | Oracle | Assertions failed | Exit code |
|---|---|---|---:|---:|
| PASS | Ажиллаж байгаа | Зөв | 0 / 16 | 0 |
| FAIL | Ажиллаж байгаа | Нэг oracle зориуд буруу | 1 / 16 | 1 |
| DOWN | Ажиллахгүй | Response ирээгүй | 16 / 16 | 1 |

PASS тест нь API-ийн хүлээгдэж буй зан төлөв зөв байгааг харуулсан. FAIL тест нь буруу oracle-г test framework илрүүлж байгааг харуулсан. DOWN тест нь application-level response биш, сервертэй холбогдох interface түвшний алдааг харуулсан.

---

## 11. Репозиторийн бүтэц

```text
lab-05/
├── .gitignore
├── README.md
├── server.js
├── generate-collection.js
├── lab05-collection.json
├── lab05-collection-fail.json
└── results/
    ├── newman-pass.txt
    ├── newman-fail.txt
    └── newman-down.txt
```

`.gitignore` файлд:

```text
.DS_Store
.idea/
node_modules/
```

оруулсан.

---

## 12. Дүгнэлт

Энэ лабораторийн хамгийн их бодол шаардсан хэсэг нь оролтуудыг зүгээр жагсаахаас илүү сонголт болон эквивалент ангид хуваагаад тэдгээрээс хэрэгтэй тестийн спецификаци сонгох байсан. Ялангуяа хэд хэдэн алдаа зэрэг үүсэх үед сервер аль алдааг эхэлж буцаах нь тодорхой биш байсан учраас үүнийг бодитоор тестлэх шаардлагатай болсон. Inactive student болон байхгүй course-ийг зэрэг ашиглахад сервер `ERROR_INACTIVE_STUDENT`-ийг түрүүлж буцааж байгааг тестээр тогтоосон.

 Зарим сонголтын хослол нь логикийн хувьд боломжгүй эсвэл ашиггүй байдаг тул бүх комбинацийг тестлэхээс илүү төлөөлөх утгуудыг зөв сонгох нь чухал байсан. Happy path-аас гадна negative болон boundary case-уудыг тусад нь шалгаснаар API-ийн зан төлөв илүү тодорхой болсон. Үндсэн PASS collection дээр шинэ application defect илрээгүй бөгөөд 16 assertion бүгд амжилттай болсон.
 
  Зориуд буруу oracle ашигласан FAIL collection нь Newman алдааг зөв илрүүлж, `exit code 1` буцааж байгааг харуулсан. Серверийг унтраасан DOWN туршилт нь assertion failure болон connection failure хоёр өөр асуудал гэдгийг ойлгоход хэрэгтэй байсан. Collection-ийг JSON файлаар хадгалж Newman-аар ажиллуулах нь Git, өөр орчин болон цаашдын CI/CD автоматжуулалтад ижил API тестүүдийг дахин ашиглахад тохиромжтой гэж үзсэн.