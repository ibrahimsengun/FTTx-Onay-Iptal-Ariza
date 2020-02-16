# FTTx-Onay-Iptal-Ariza
Türk Telekom yaz stajında geliştirdiğim uygulama

Bu proje Türk Telekom Malatya İl Genel Müdürlüğünde kullanılmaya devam eden bir projedir. Paylaşacağım kodlar gerçek veritabanı ile ilişkili değildir.

Android ve Web olarak iki alanda çalışan proje Android tarafından gönderilen verilerin Web tarafında görüntülenmesi ve işlenmesine dayanıyor. Farklı ekiplerin birbirleriyle iletişimini ve veri transferini sağlıyor.

## Android
Bu kısımda [ZXing](https://github.com/zxing/zxing) kullanılıp bir barkod okuyucu sağlandı. Bu sayede ekipler barkod numaralarını hatasız şekilde gerekli ekiplere iletebiliyor. Java ile geliştirilen uygulama nesne yönelimli olarak yazılmıştır. Barkod okuma, fotoğraf çekme ve sisteme yükleme, kullanıcı girişi gibi yetenekleri vardır.

## Web
Bu kısımda HTML, CSS, Javascirpt ve JQuery ile Front-End tasarlanmıştır. Back-End ise Firebase kullanıldığı için Nodejs ile yapılmıştır. Gönderilen verilerin düzenli bir şekilde gösterilmesi için JQuery'nin [Datatables](https://datatables.net/) isimli kütüphanesi kullanılmıştır. Web üzerinden ekiplere iş emirleri gönderebilir ve gönderilen işlerin takibini yapılabilir.

## Veritabanı
Firebase'in Firestore isimli veritabanı projenin her iki tarafınıda bilgilerin kaydedilip okunmasını sağlamıştır.

## Depolama
Depolama kısmında Firebase'in Storage kullanılmıştır. Ekiplere dağıtılacak olan iş emirleri png formatında Web üzerinden gönderilir ve Android tarafında görüntülernir. Bu png dosyaları Firebase Storage'da saklanır. Aynı zamanda Android tarafından ekipler sahadan fotoğrafları Web tarafında görüntülenmesi için gönderebilir. Bu fotoğraflar da aynı şekilde Storage'da saklanır. Bu sayede günlük indirme ve yükleme işlemlerinin istatistiki verileri Firebase sayesinde izlenebilir durumdadır.

## Kimlik Doğrulama
Her kullanıcı TC Kimlik numaraları ve kendilerine özel şifreler ile giriş yapmaktadır. Doğrulama işlemi ise Firebase Authentication ile sağlanmaktadır. Bu sayede günlük kullanıcı sayısı ve benzeri veriler Firebase tarafında tutulmuş oluyor.
