cd ~/Documents/GitHub/CSA313_B232270042_Main

mkdir -p lab-07/{bugs,evidence,screenshots}
cd lab-07

cat > .gitignore <<'EOF'
.DS_Store
.idea/
node_modules/
app.js
app.log
.app.pid
.timer.pid
EOF

# Put the app.js supplied by the teacher into this directory.
# If it is in Downloads:
if [ -f ~/Downloads/app.js ]; then
    cp ~/Downloads/app.js .
fi

# Stop here if app.js isn't present.
test -f app.js || {
    echo "ERROR: Copy the teacher's app.js into:"
    pwd
    exit 1
}

echo "Node:"
node -v

echo
echo "Browser:"
if command -v firefox >/dev/null; then
    firefox --version
elif command -v chromium >/dev/null; then
    chromium --version
elif command -v google-chrome >/dev/null; then
    google-chrome --version
else
    echo "Browser version not found from terminal"
fi

cat > charter.md <<'EOF'
# Charter

**Судлах:** Уншлагын жагсаалт аппын ном нэмэх, хайх, устгах, тоолуур, navigation болон API-ийн үндсэн ажиллагааг судална.

**Ашиглан:** хүчинтэй болон буруу оролт, хоосон утга, boundary утга, том/жижиг үсгийн ялгаа, урт текст болон API-д зориуд буруу хүсэлт ашиглана. GUI болон API түвшинд exploratory тест хийнэ.

**Илрүүлэх:** validation-ийн алдаа, өгөгдлийн зөрүү, буруу мессеж, хайлтын алдаа, UI-ийн ойлгомжгүй байдал, буруу HTTP status болон хэрэглэгчийн харах мэдээлэл specification-тай зөрсөн тохиолдлыг илрүүлнэ.

## Сонгосон аялалууд

### Supermodel
UI-г хэрэглэгчийн нүдээр шалгаж, харагдах байдал, товч, хүснэгт, мессеж болон ерөнхий interaction дээр асуудал байгаа эсэхийг судална. Энэ апп нь үндсэндээ browser UI тул энэ аялал тохиромжтой.

### Couch Potato
Хоосон болон дутуу input, энгийн хэрэглэгчийн буруу үйлдлүүдийг туршина. Гарчиг, зохиолч, он нь бүгд заавал байх specification-тэй тул validation шалгахад тохиромжтой.

### Saboteur
API руу curl ашиглан malformed JSON болон хэвийн бус хүсэлт явуулж server error handling-ийг шалгана. Specification-д malformed JSON нь HTTP 400 буцаах ёстой гэж тодорхой заасан.

### Fed-Ex
Өгөгдөл UI → API → хүснэгт → статистик гэсэн замаар зөв дамжиж байгаа эсэхийг ажиглана. Номын жагсаалт болон "Нийт ном" тоолуур хоорондоо таарч байгаа эсэхийг шалгахад тохиромжтой.
EOF

git add .gitignore charter.md
git commit -m "add Lab07 exploratory testing charter"
git push

echo
echo "=== IMPORTANT: charter commit ==="
git log -1 --format='%h %ad %s' --date=iso -- charter.md