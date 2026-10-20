# BUG-03: Malformed JSON хүсэлт HTTP 400-ийн оронд HTTP 200 буцааж байна

**Орчин:** Уншлагын жагсаалт v2.0, Mozilla Firefox 157.0, Fedora Linux, 2026-10-20  
**Урьдчилсан нөхцөл:** `node app.js`-ийг шинээр асаасан.

## Дахин гаргах алхам

1. Аппыг шинээр асаана.
2. Terminal дээр:

```bash
curl -i -X POST http://localhost:3000/api/books \
  -H 'Content-Type: application/json' \
  --data-binary 'THIS IS NOT VALID JSON'
```

командыг ажиллуулна.

## Хүлээгдэж байсан

Specification-ийн дагуу malformed JSON body бүхий POST хүсэлт **HTTP 400** буцаах ёстой.

## Бодит үр дүн

Server:

```text
HTTP/1.1 200 OK
...
{"result":"OK"}
```

гэж буцаасан.

**Ноцтой байдал:** Major — invalid API request амжилттай мэт хариу авч байна.

**Хавсралт:** `evidence/bug-03-curl.txt`

**Олдсон аялал:** Saboteur
