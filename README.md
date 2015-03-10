# 아기랑 나가도 돼요?
## 설명
서울시의 미세먼지와 초미세먼지 농도를 1시간 간격으로 정보를 가져와 상태바(Status Bar)에 알림(Notification)으로 알려준다.

색상과 물개의 표정으로 표현한다.

|색상|등급|이미지|
|---|---|---|
|파랑(Blue)|좋음|![좋음](./app/src/main/res/drawable-ldpi/icon_good.png)|
|초록(Green)|보통|![보통](./app/src/main/res/drawable-ldpi/icon_normal.png)|
|노랑(Yellow)|안 좋음|![안 좋음](./app/src/main/res/drawable-ldpi/icon_bad.png)|
|주황(Orange)|꽤 안 좋음|![꽤 안 좋음](./app/src/main/res/drawable-ldpi/icon_worse.png)|
|빨강(Red)|아주 안 좋음|![아주 안 좋음](./app/src/main/res/drawable-ldpi/icon_worst.png)|

## TODO
1. 전국적 지원 : data.go.kr open API 신청 완료(테스트용 300건, 실 서비스 10,000건)
2. 서버 선택 : node.js(진혁이 서버) or parse.com 사용
3. 디자이너 킴 디자인 적용 작업.

