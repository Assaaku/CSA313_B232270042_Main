# Лаборатори №7 — Exploratory тест / SBTM

## Оюутны мэдээлэл

- **Нэр:** Б.Тэмүүлэн
- **Оюутны код:** B232270042
- **Хичээл:** F.CSA313 — Программ хангамжийн чанарын баталгаа ба тест
- **Node.js:** `v22.22.2`
- **Browser:** Mozilla Firefox 157.0
- **OS:** Fedora Linux

## Зорилго

Энэ лабораторийн ажлаар багшийн өгсөн Уншлагын жагсаалт v2.0 апп дээр Session-Based Test Management аргаар exploratory тест хийсэн. Session эхлэхээс өмнө charter бичиж Git-д commit хийсэн бөгөөд үндсэн exploratory session-ийг 60 минут хийсэн.

Сонгосон аяллууд:

- **Supermodel** — GUI, хүснэгт, хайлт, товч болон хэрэглэгчийн харж буй мэдээллийг шалгах.
- **Saboteur** — curl ашиглан API-д хэвийн бус хүсэлт явуулж error handling-ийг шалгах.

Үндсэн session:

```text
2026-10-20, 22:06 - 23:06
```

23:06-аас хойш зарим олдсон согогийг fresh app дээр дахин гаргаж баталгаажуулсан.

## Session файлууд

- `charter.md` — session эхлэхээс өмнөх төлөвлөгөө
- `session-report.md` — timestamp бүхий exploratory session тэмдэглэл
- `bugs/` — согог тус бүрийн дэлгэрэнгүй тайлан
- `screenshots/` — UI согогуудын нотолгоо
- `evidence/` — API болон нэмэлт текст нотолгоо

## Олдсон согогууд

| ID | Товч тайлбар | Түвшин | Аялал |
|---|---|---|---|
| BUG-01 | "Хуучин хувилбар" холбоос ажиллахгүй | UI | Supermodel |
| BUG-02 | Search том/жижиг үсэг ялгана | UI | Supermodel |
| BUG-03 | Malformed JSON HTTP 400 биш HTTP 200 буцаана | API | Saboteur |
| BUG-04 | Устгах товч улаан биш | UI | Supermodel |
| BUG-05 | "Нийт ном" тоолуур хүснэгттэй таарахгүй | Data/UI | Supermodel |
| BUG-06 | Устгасны дараах мессеж буруу | UI | Supermodel |

## API evidence

BUG-03 дээр malformed JSON хүсэлтэд:

```text
HTTP/1.1 200 OK
...
{"result":"OK"}
```

хариу ирсэн. Specification-ийн дагуу HTTP 400 байх ёстой.

Бүтэн `curl -i` гаралт:

```text
evidence/bug-03-curl.txt
```

## Дүгнэлт

Энэ session-д Supermodel аялал хамгийн олон хэрэглэгчийн түвшний согог илрүүлэхэд үр дүнтэй байлаа. BUG-01, BUG-02, BUG-04, BUG-05 болон BUG-06 нь UI болон хэрэглэгчийн харж буй мэдээллийг шалгах явцад илэрсэн. Saboteur аяллаар API-д malformed JSON илгээхэд BUG-03 илэрсэн бөгөөд HTTP 400-ийн оронд HTTP 200 буцаж байсан.

 Charter-аас гадуур session-ийн явцад хүснэгт, тоолуур болон үйлдлийн дараах feedback-ийн consistency-г илүү дэлгэрэнгүй шалгасан. Дараагийн session-д маш урт Unicode оролт, олон add/delete дараалал болон duplicate-ийн өөр casing-уудыг илүү гүн шалгаж болно. BUG-01, BUG-02 болон BUG-05-ийг Lab 1-ийн Playwright тестээр navigation, search result болон DOM дээрх тоог assertion хийж автоматжуулах боломжтой. 
 
 BUG-03 нь UI тестээс илүү API-level automated test-д тохиромжтой. BUG-04 шиг өнгө болон visual affordance-ийн асуудлыг exploratory тестээр хүний нүдээр хурдан анзаарах боломжтой боловч visual regression тестээр мөн автоматжуулж болно. Энэ лаборатори нь scripted test-ээс гадна charter болон хэрэглэгчийн бодит зан төлөв дээр тулгуурласан exploratory testing нь өөр төрлийн согог илрүүлэхэд хэрэгтэйг харуулсан.

