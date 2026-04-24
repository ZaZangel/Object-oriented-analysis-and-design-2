# ─────────────────────────────────────────────────────────────
# ИМПОРТЫ: подключаем необходимые модули
# ─────────────────────────────────────────────────────────────

# Flask — основной фреймворк для создания веб-приложения
from flask import Flask, jsonify, request, send_from_directory, send_file
# CORS — разрешает браузеру делать запросы с другого домена/порта
from flask_cors import CORS
# ABC и abstractmethod — нужны для создания абстрактных классов (интерфейсов)
# Это основа паттерна: задаём контракт, который обязаны реализовать все итераторы
from abc import ABC, abstractmethod
# os — работа с путями к файлам и папкам
import os


# ─────────────────────────────────────────────────────────────
# ИНИЦИАЛИЗАЦИЯ FLASK-ПРИЛОЖЕНИЯ
# ─────────────────────────────────────────────────────────────

# Создаём экземпляр Flask-приложения
# __name__ — имя текущего модуля (нужно Flask для настройки)
# static_folder="static" — папка, откуда сервер будет отдавать статические файлы (HTML, CSS, JS)
app = Flask(__name__, static_folder="static")

# Включаем поддержку CORS, чтобы фронтенд мог делать запросы к этому серверу
# Без этого браузер может блокировать запросы из-за политики безопасности (Same-Origin Policy)
CORS(app)


# ─────────────────────────────────────────────────────────────
# КЛАСС Recipe: модель данных "Рецепт" (НЕ МЕНЯЕТСЯ)
# ─────────────────────────────────────────────────────────────

class Recipe:
    """
    Класс, представляющий один рецепт.
    Это простая модель данных (Data Class) — только хранение и форматирование.
    Не зависит от паттерна Итератор.
    """
    
    # Конструктор: вызывается при создании нового объекта Recipe
    def __init__(self, title: str, chef: str, prep_time: int, difficulty: str, image: str = ""):
        self.title = title          # Название рецепта
        self.chef = chef            # Автор рецепта
        self.prep_time = prep_time  # Время приготовления в минутах
        self.difficulty = difficulty  # Уровень сложности: "easy", "medium", "hard"
        self.image = image          # Имя файла изображения

    # Метод для преобразования объекта Recipe в словарь (для отправки в JSON)
    def to_dict(self):
        return {
            "title": self.title,
            "chef": self.chef,
            "prep_time": self.prep_time,
            # Форматируем время: "1ч 30м" или "45м"
            "prep_time_str": f"{self.prep_time // 60}ч {self.prep_time % 60:02d}м" if self.prep_time >= 60 else f"{self.prep_time}м",
            "difficulty": self.difficulty,
            # Перевод сложности на русский для интерфейса
            "difficulty_ru": {"easy": "Лёгкий", "medium": "Средний", "hard": "Сложный"}.get(self.difficulty, self.difficulty),
            "image": self.image
        }


# ═══════════════════════════════════════════════════════════
# 🎯 ЧАСТЬ 1: АБСТРАКЦИЯ ИТЕРАТОРА (Интерфейс)
# ═══════════════════════════════════════════════════════════

# RecipeIterator — абстрактный класс, который задаёт "контракт" для всех итераторов
# (ABC = Abstract Base Class)
class RecipeIterator(ABC):
    """
    🔑 КЛЮЧЕВОЙ ЭЛЕМЕНТ ПАТТЕРНА: Интерфейс Итератора
    
    Все конкретные итераторы ОБЯЗАНЫ реализовать эти методы.
    Благодаря этому клиентский код может работать с ЛЮБЫМ итератором,
    не зная его конкретного типа (принцип полиморфизма).
    """
    
    # @abstractmethod — декоратор, который делает метод обязательным для переопределения
    # Если подкласс не реализует этот метод — будет ошибка при создании объекта
    
    # Проверка: есть ли следующий элемент для обхода?
    @abstractmethod
    def has_next(self) -> bool: 
        pass  # pass означает "пустая реализация" — только сигнатура метода

    # Перейти к следующему элементу и вернуть его
    @abstractmethod
    def next(self) -> Recipe: 
        pass

    # Проверка: есть ли предыдущий элемент?
    @abstractmethod
    def has_previous(self) -> bool: 
        pass

    # Перейти к предыдущему элементу и вернуть его
    @abstractmethod
    def previous(self) -> Recipe: 
        pass

    # Сбросить позицию итератора на начало
    @abstractmethod
    def reset(self): 
        pass

    # Получить текущий индекс (позицию) в последовательности
    @abstractmethod
    def current_index(self) -> int: 
        pass

    # Получить общее количество элементов в коллекции
    @abstractmethod
    def total(self) -> int: 
        pass

    # Получить текущий элемент (без перемещения)
    @abstractmethod
    def current(self) -> Recipe: 
        pass


# ═══════════════════════════════════════════════════════════
# 🎯 ЧАСТЬ 2: КОНКРЕТНЫЕ ИТЕРАТОРЫ (Реализации)
# ═══════════════════════════════════════════════════════════

# ── Итератор 1: Хронологический (по порядку добавления) ──
class ChronologicalIterator(RecipeIterator):
    """
    Конкретная реализация итератора: обход в порядке добавления рецептов.
    
    🔹 Наследуется от RecipeIterator → обязан реализовать все абстрактные методы
    🔹 Простая логика: работаем напрямую с исходным списком _recipes
    """
    
    # Конструктор: принимает список рецептов для обхода
    def __init__(self, recipes: list[Recipe]):
        # Сохраняем ссылку на исходный список (не копию — экономим память)
        self._recipes = recipes
        # Инициализируем индекс позиции: начинаем с первого элемента (индекс 0)
        self._index = 0

    # Реализация абстрактного метода has_next()
    def has_next(self) -> bool:
        # Есть следующий элемент, если текущий индекс меньше последнего возможного
        # len(self._recipes) - 1 — индекс последнего элемента
        return self._index < len(self._recipes) - 1

    # Реализация абстрактного метода next()
    def next(self) -> Recipe:
        # Если есть следующий элемент — сдвигаем индекс вперёд
        if self.has_next():
            self._index += 1
        # Возвращаем рецепт по новому индексу
        return self._recipes[self._index]

    # Реализация абстрактного метода has_previous()
    def has_previous(self) -> bool:
        # Есть предыдущий элемент, если индекс больше 0 (не на первом элементе)
        return self._index > 0

    # Реализация абстрактного метода previous()
    def previous(self) -> Recipe:
        # Если есть предыдущий элемент — сдвигаем индекс назад
        if self.has_previous():
            self._index -= 1
        # Возвращаем рецепт по новому индексу
        return self._recipes[self._index]

    # Реализация абстрактного метода reset()
    def reset(self):
        # Сбрасываем индекс на начало списка
        self._index = 0

    # Реализация абстрактного метода current_index()
    def current_index(self) -> int:
        # Возвращаем текущую позицию
        return self._index

    # Реализация абстрактного метода total()
    def total(self) -> int:
        # Возвращаем общее количество рецептов
        return len(self._recipes)

    # Реализация абстрактного метода current()
    def current(self) -> Recipe:
        # Возвращаем текущий рецепт по индексу
        return self._recipes[self._index]


# ── Итератор 2: По сложности (от лёгкого к сложному) ──
class DifficultyIterator(RecipeIterator):
    """
    Конкретная реализация итератора: обход по возрастанию сложности.
    
    🔹 Логика сортировки инкапсулирована ВНУТРИ этого класса
    🔹 Коллекция Cookbook НЕ знает, как происходит сортировка
    """
    
    # Словарь для преобразования строки сложности в число для сортировки
    # Чем меньше число — тем раньше рецепт появится в списке
    DIFF_ORDER = {"easy": 0, "medium": 1, "hard": 2}

    # Конструктор
    def __init__(self, recipes: list[Recipe]):
        # Сохраняем ссылку на оригинальный список (может понадобиться)
        self._original = recipes
        # Создаём ОТСОТИРОВАННУЮ копию списка:
        # - key=lambda r: ... — функция, которая вычисляет "вес" элемента для сортировки
        # - Сначала сортируем по сложности (DIFF_ORDER), потом по названию (для стабильности)
        # - sorted() возвращает НОВЫЙ список, оригинал не меняется
        self._sorted = sorted(recipes, key=lambda r: (self.DIFF_ORDER.get(r.difficulty, 99), r.title))
        # Начинаем обход с первого элемента отсортированного списка
        self._index = 0

    # Все методы ниже аналогичны ChronologicalIterator, но работают с self._sorted
    def has_next(self) -> bool:
        return self._index < len(self._sorted) - 1

    def next(self) -> Recipe:
        if self.has_next():
            self._index += 1
        return self._sorted[self._index]

    def has_previous(self) -> bool:
        return self._index > 0

    def previous(self) -> Recipe:
        if self.has_previous():
            self._index -= 1
        return self._sorted[self._index]

    def reset(self):
        # При сбросе просто возвращаемся в начало отсортированного списка
        # (в отличие от ShuffleIterator, здесь не нужно пересортировывать)
        self._index = 0

    def current_index(self) -> int:
        return self._index

    def total(self) -> int:
        # Возвращаем длину отсортированного списка (она равна оригиналу)
        return len(self._sorted)

    def current(self) -> Recipe:
        return self._sorted[self._index]


# ── Итератор 3: Сначала быстрые (по времени приготовления) ──
class QuickFirstIterator(RecipeIterator):
    """
    Конкретная реализация итератора: обход по возрастанию времени приготовления.
    
    🔹 Демонстрирует, как легко добавить новый алгоритм обхода:
       просто создаём новый класс, не меняя существующие!
    """
    
    def __init__(self, recipes: list[Recipe]):
        self._original = recipes
        # Сортируем по времени приготовления (prep_time), затем по названию
        self._sorted = sorted(recipes, key=lambda r: (r.prep_time, r.title))
        self._index = 0

    # Методы идентичны предыдущим итераторам — работает полиморфизм!
    def has_next(self) -> bool:
        return self._index < len(self._sorted) - 1

    def next(self) -> Recipe:
        if self.has_next():
            self._index += 1
        return self._sorted[self._index]

    def has_previous(self) -> bool:
        return self._index > 0

    def previous(self) -> Recipe:
        if self.has_previous():
            self._index -= 1
        return self._sorted[self._index]

    def reset(self):
        self._index = 0

    def current_index(self) -> int:
        return self._index

    def total(self) -> int:
        return len(self._sorted)

    def current(self) -> Recipe:
        return self._sorted[self._index]


# ═══════════════════════════════════════════════════════════
# 🎯 ЧАСТЬ 3: АБСТРАКЦИЯ КОЛЛЕКЦИИ (Интерфейс)
# ═══════════════════════════════════════════════════════════

# RecipeCollection — абстрактный класс для коллекций, поддерживающих итерацию
# В данном проекте используется одна реализация (Cookbook), но интерфейс
# позволяет в будущем добавить другие типы коллекций (например, "Избранное")
class RecipeCollection(ABC):
    """
    🔑 КЛЮЧЕВОЙ ЭЛЕМЕНТ ПАТТЕРНА: Интерфейс Коллекции
    
    Определяет единственный обязательный метод: создать итератор.
    Это реализует принцип Dependency Inversion: 
    клиент зависит от абстракции, а не от конкретной реализации.
    """
    
    # Фабричный метод: создаёт и возвращает итератор нужного типа
    # mode — строка-идентификатор режима обхода
    @abstractmethod
    def create_iterator(self, mode: str) -> RecipeIterator: 
        pass


# ═══════════════════════════════════════════════════════════
# 🎯 ЧАСТЬ 4: КОНКРЕТНАЯ КОЛЛЕКЦИЯ (Реализация)
# ═══════════════════════════════════════════════════════════

# ── Конкретная коллекция: Книга рецептов ──
class Cookbook(RecipeCollection):
    """
    Конкретная реализация коллекции рецептов.
    
    🔹 Наследуется от RecipeCollection → обязана реализовать create_iterator()
    🔹 Отвечает ТОЛЬКО за хранение данных (принцип Single Responsibility)
    🔹 НЕ знает, как происходит обход — делегирует это итераторам
    """
    
    # Конструктор
    def __init__(self, name: str):
        self._name = name                    # Название книги
        self._recipes: list[Recipe] = []     # Внутренний список рецептов (инкапсулирован)

    # Геттер для названия (property позволяет обращаться как к атрибуту)
    @property
    def name(self) -> str:
        return self._name

    # 🔥 РЕАЛИЗАЦИЯ АБСТРАКТНОГО МЕТОДА: Фабрика итераторов
    def create_iterator(self, mode: str) -> RecipeIterator:
        """
        Фабричный метод: создаёт нужный тип итератора в зависимости от режима.
        
        🔹 Это ЕДИНСТВЕННОЕ место в классе, которое "знает" о конкретных итераторах
        🔹 Чтобы добавить новый режим: добавляем одну ветку if/elif, не меняя остальной код
        🔹 Принцип Open/Closed: открыто для расширения, закрыто для модификации
        """
        if mode == "chronological":
            # Создаём и возвращаем итератор хронологического обхода
            return ChronologicalIterator(self._recipes)
        elif mode == "by_difficulty":
            # Создаём и возвращаем итератор обхода по сложности
            return DifficultyIterator(self._recipes)
        elif mode == "quick_first":
            # Создаём и возвращаем итератор обхода по времени
            return QuickFirstIterator(self._recipes)
        else:
            # Если передан неизвестный режим — выбрасываем исключение
            raise ValueError(f"Неизвестный режим итерации: {mode}")

    # Метод добавления рецепта в коллекцию
    def add_recipe(self, recipe: Recipe):
        # Добавляем в конец списка
        self._recipes.append(recipe)
        # Примечание: в этой простой версии не нужно очищать кэш,
        # потому что итераторы создают свои копии при инициализации

    # Метод удаления рецепта по индексу
    def remove_recipe(self, index: int):
        # Проверка границ для безопасности
        if 0 <= index < len(self._recipes):
            self._recipes.pop(index)

    # Метод получения копии списка всех рецептов (для отображения в интерфейсе)
    def get_recipes(self) -> list[Recipe]:
        # list() создаёт поверхностную копию — клиент не сможет изменить оригинал
        return list(self._recipes)


# ─────────────────────────────────────────────────────────────
# ИНИЦИАЛИЗАЦИЯ ПРИЛОЖЕНИЯ: создание данных и настройка
# ─────────────────────────────────────────────────────────────

# Получаем абсолютный путь к папке "images" рядом с этим файлом
IMAGES_DIR = os.path.join(os.path.dirname(os.path.abspath(__file__)), "images")

# Создаём папку images, если она ещё не существует
os.makedirs(IMAGES_DIR, exist_ok=True)

# Создаём экземпляр книги рецептов
cookbook = Cookbook("Домашняя кухня")

# Добавляем тестовые рецепты
cookbook.add_recipe(Recipe("Омлет с травами", "Анна К.", 15, "easy", "omelet.jpg"))
cookbook.add_recipe(Recipe("Борщ классический", "Иван П.", 120, "medium", "borscht.jpg"))
cookbook.add_recipe(Recipe("Суфле из курицы", "Мария С.", 45, "medium", "souffle.jpg"))
cookbook.add_recipe(Recipe("Тирамису", "Лука Р.", 180, "hard", "tiramisu.jpg"))
cookbook.add_recipe(Recipe("Салат Цезарь", "Елена В.", 30, "easy", "caesar.jpg"))

# 🔥 КЛЮЧЕВОЙ МОМЕНТ: создаём итератор через фабричный метод коллекции
# Переменная имеет тип абстракции RecipeIterator, а не конкретного класса
# Это позволяет позже заменить итератор на любой другой без изменения кода ниже
current_iterator: RecipeIterator = cookbook.create_iterator("chronological")

# Глобальная переменная: статус "готовим/не готовим"
is_cooking: bool = False


# ─────────────────────────────────────────────────────────────
# ВСПОМОГАТЕЛЬНАЯ ФУНКЦИЯ: формирование ответа для фронтенда
# ─────────────────────────────────────────────────────────────

# Функция собирает все данные для отправки клиенту
# 🔹 Обратите внимание: работает с current_iterator через абстрактный интерфейс!
# 🔹 Не важно, какой конкретно итератор — методы те же самые (полиморфизм)
def get_state():
    # Получаем текущий рецепт, вызывая метод current() у итератора
    # Тип возвращаемого значения — Recipe, независимо от типа итератора
    recipe = current_iterator.current()
    
    return {
        "recipe": recipe.to_dict(),                    # Данные текущего рецепта
        "index": current_iterator.current_index(),     # Текущая позиция
        "total": current_iterator.total(),             # Общее количество
        "has_next": current_iterator.has_next(),       # Флаг для кнопки "Вперёд"
        "has_previous": current_iterator.has_previous(), # Флаг для кнопки "Назад"
        "is_cooking": is_cooking,                      # Статус анимации
        "cookbook_name": cookbook.name,                # Название книги
        # Полный список рецептов (получаем напрямую из коллекции, не через итератор)
        "recipes": [r.to_dict() for r in cookbook.get_recipes()],
    }


# ─────────────────────────────────────────────────────────────
# API ENDPOINTS: маршруты (URL), на которые реагирует сервер
# ─────────────────────────────────────────────────────────────

# Маршрут для главной страницы
@app.route("/")
def index():
    # Отдаём HTML-файл из папки static
    return send_file("static/index.html")

# Маршрут для получения текущего состояния (GET)
@app.route("/api/state")
def api_state():
    # jsonify превращает словарь в JSON с правильными заголовками
    return jsonify(get_state())

# Маршрут для перехода к следующему рецепту (POST)
@app.route("/api/next", methods=["POST"])
def api_next():
    global is_cooking
    # 🔹 Вызываем next() у итератора — не важно, какой он конкретно!
    # 🔹 Код одинаковый для Chronological/Difficulty/QuickFirst итераторов
    current_iterator.next()
    is_cooking = True
    return jsonify(get_state())

# Маршрут для перехода к предыдущему рецепту
@app.route("/api/previous", methods=["POST"])
def api_previous():
    global is_cooking
    current_iterator.previous()  # Полиморфный вызов
    is_cooking = True
    return jsonify(get_state())

# Маршрут для переключения статуса "готовим/пауза"
@app.route("/api/start", methods=["POST"])
def api_start():
    global is_cooking
    is_cooking = not is_cooking  # Инверсия булева значения
    return jsonify(get_state())

# Маршрут для смены режима обхода: /api/mode/by_difficulty и т.д.
@app.route("/api/mode/<mode>", methods=["POST"])
def api_mode(mode):
    global current_iterator
    # 🔥 КЛЮЧЕВОЙ МОМЕНТ: создаём НОВЫЙ итератор нужного типа
    # Старый итератор будет удалён сборщиком мусора (garbage collector)
    # Переменная current_iterator теперь ссылается на объект другого класса,
    # но интерфейс тот же — код ниже не нужно менять!
    current_iterator = cookbook.create_iterator(mode)
    return jsonify(get_state())

# Маршрут для сброса позиции итерации
@app.route("/api/reset", methods=["POST"])
def api_reset():
    # Вызываем reset() у текущего итератора (полиморфно)
    current_iterator.reset()
    return jsonify(get_state())

# Маршрут для раздачи изображений
@app.route("/images/<filename>")
def serve_image(filename):
    # send_from_directory — безопасная отдача файлов
    return send_from_directory(IMAGES_DIR, filename)


# ─────────────────────────────────────────────────────────────
# ТОЧКА ВХОДА: запуск сервера
# ─────────────────────────────────────────────────────────────

if __name__ == "__main__":
    print("=" * 50)
    print("  🍳 Cookbook (Iterator Pattern)")
    print("  http://localhost:5000")
    print("=" * 50)
    # Запускаем сервер на порту 5000 с отладкой
    app.run(debug=True, port=5000)