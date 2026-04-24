from flask import Flask, jsonify, request, send_from_directory, send_file
from flask_cors import CORS
import os

app = Flask(__name__, static_folder="static")
CORS(app)


class Recipe:
    def __init__(self, title: str, chef: str, prep_time: int, difficulty: str, image: str = ""):
        self.title = title
        self.chef = chef
        self.prep_time = prep_time
        self.difficulty = difficulty
        self.image = image

    def to_dict(self):
        return {
            "title": self.title,
            "chef": self.chef,
            "prep_time": self.prep_time,
            "prep_time_str": f"{self.prep_time // 60}ч {self.prep_time % 60:02d}м" if self.prep_time >= 60 else f"{self.prep_time}м",
            "difficulty": self.difficulty,
            "difficulty_ru": {"easy": "Лёгкий", "medium": "Средний", "hard": "Сложный"}.get(self.difficulty, self.difficulty),
            "image": self.image
        }


class Cookbook:
    """Коллекция рецептов БЕЗ паттерна Итератор — вся логика внутри"""
    DIFF_ORDER = {"easy": 0, "medium": 1, "hard": 2}

    def __init__(self, name: str):
        self._name = name
        self._recipes: list[Recipe] = []
        self._index: int = 0
        self._mode: str = "chronological"
        self._cache: dict[str, list[Recipe]] = {}

    @property
    def name(self) -> str:
        return self._name

    def add_recipe(self, recipe: Recipe):
        self._recipes.append(recipe)
        self._cache.clear()  # сброс кэша при изменении

    def remove_recipe(self, index: int):
        if 0 <= index < len(self._recipes):
            self._recipes.pop(index)
            self._cache.clear()

    def get_recipes(self) -> list[Recipe]:
        return list(self._recipes)

    def _get_sorted_list(self, mode: str) -> list[Recipe]:
        """Возвращает список рецептов в нужном порядке (с кэшированием)"""
        if mode in self._cache:
            return self._cache[mode]

        if mode == "chronological":
            result = list(self._recipes)
        elif mode == "by_difficulty":
            result = sorted(self._recipes, key=lambda r: (self.DIFF_ORDER.get(r.difficulty, 99), r.title))
        elif mode == "quick_first":
            result = sorted(self._recipes, key=lambda r: (r.prep_time, r.title))
        else:
            result = list(self._recipes)

        self._cache[mode] = result
        return result

    def set_mode(self, mode: str):
        if mode not in ("chronological", "by_difficulty", "quick_first"):
            raise ValueError(f"Неизвестный режим: {mode}")
        self._mode = mode
        self._index = 0

    def has_next(self) -> bool:
        return self._index < len(self._get_sorted_list(self._mode)) - 1

    def next(self) -> Recipe:
        recipes = self._get_sorted_list(self._mode)
        if self.has_next():
            self._index += 1
        return recipes[self._index]

    def has_previous(self) -> bool:
        return self._index > 0

    def previous(self) -> Recipe:
        recipes = self._get_sorted_list(self._mode)
        if self.has_previous():
            self._index -= 1
        return recipes[self._index]

    def current(self) -> Recipe:
        return self._get_sorted_list(self._mode)[self._index]

    def current_index(self) -> int:
        return self._index

    def total(self) -> int:
        return len(self._recipes)

    def reset(self):
        self._index = 0


# ── Инициализация ──
IMAGES_DIR = os.path.join(os.path.dirname(os.path.abspath(__file__)), "images")
os.makedirs(IMAGES_DIR, exist_ok=True)

cookbook = Cookbook("Домашняя кухня")
cookbook.add_recipe(Recipe("Омлет с травами", "Анна К.", 15, "easy", "omelet.jpg"))
cookbook.add_recipe(Recipe("Борщ классический", "Иван П.", 120, "medium", "borscht.jpg"))
cookbook.add_recipe(Recipe("Суфле из курицы", "Мария С.", 45, "medium", "souffle.jpg"))
cookbook.add_recipe(Recipe("Тирамису", "Лука Р.", 180, "hard", "tiramisu.jpg"))
cookbook.add_recipe(Recipe("Салат Цезарь", "Елена В.", 30, "easy", "caesar.jpg"))

is_cooking: bool = False


def get_state():
    recipe = cookbook.current()
    return {
        "recipe": recipe.to_dict(),
        "index": cookbook.current_index(),
        "total": cookbook.total(),
        "has_next": cookbook.has_next(),
        "has_previous": cookbook.has_previous(),
        "is_cooking": is_cooking,
        "cookbook_name": cookbook.name,
        "recipes": [r.to_dict() for r in cookbook.get_recipes()],
    }


# ── API Endpoints (аналогично) ──
@app.route("/")
def index():
    return send_file("static/index.html")

@app.route("/api/state")
def api_state():
    return jsonify(get_state())

@app.route("/api/next", methods=["POST"])
def api_next():
    global is_cooking
    cookbook.next()
    is_cooking = True
    return jsonify(get_state())

@app.route("/api/previous", methods=["POST"])
def api_previous():
    global is_cooking
    cookbook.previous()
    is_cooking = True
    return jsonify(get_state())

@app.route("/api/start", methods=["POST"])
def api_start():
    global is_cooking
    is_cooking = not is_cooking
    return jsonify(get_state())

@app.route("/api/mode/<mode>", methods=["POST"])
def api_mode(mode):
    cookbook.set_mode(mode)
    return jsonify(get_state())

@app.route("/api/reset", methods=["POST"])
def api_reset():
    cookbook.reset()
    return jsonify(get_state())

@app.route("/images/<filename>")
def serve_image(filename):
    return send_from_directory(IMAGES_DIR, filename)


if __name__ == "__main__":
    print("=" * 50)
    print("  🍳 Cookbook (БЕЗ паттерна)")
    print("  http://localhost:50001")
    print("=" * 50)
    app.run(debug=True, port=50001)