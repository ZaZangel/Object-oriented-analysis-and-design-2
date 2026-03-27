Разработанный проект демонстрирует работу паттерна «Заместитель» (Proxy) на языке C++ с использованием графической библиотеки SFML.
Приложение позволяет наглядно сравнить два подхода: ленивую загрузку объектов через прокси-класс и немедленную инициализацию тяжелых ресурсов без оптимизации.
Класс ProxyDocument выступает легковесным посредником, который создает реальный объект документа только при первом обращении к нему, что экономит память и ускоряет запуск программы.
<img width="1207" height="682" alt="image" src="https://github.com/user-attachments/assets/9de03dd9-6e65-4c50-b415-80926e34761a" />

Вот интерфейс.

<img width="815" height="641" alt="image" src="https://github.com/user-attachments/assets/9317135e-412c-4786-b05a-2571025fcf04" />
<img width="804" height="599" alt="image" src="https://github.com/user-attachments/assets/1582e4b1-ed38-4d2c-8546-3ee55a0d3654" />
<img width="795" height="583" alt="image" src="https://github.com/user-attachments/assets/a35a9a27-eb49-4f1f-907d-4363f3f3b121" />
