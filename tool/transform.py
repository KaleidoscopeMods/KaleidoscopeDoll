import copy
import json
import os
import re
import shutil
from pypinyin import pinyin, Style


class CompactJSONEncoder(json.JSONEncoder):
    """自定义JSON编码器，实现紧凑格式化"""

    def __init__(self, *args, **kwargs):
        super().__init__(*args, **kwargs)
        self.current_indent = 0
        self.current_indent_str = ""

    def encode(self, obj):
        if isinstance(obj, (list, tuple)):
            if len(obj) == 0:
                return "[]"
            # 如果是简单数组（数字、字符串），放在一行
            if all(isinstance(item, (int, float, str, bool)) or item is None for item in obj):
                return "[" + ", ".join(json.dumps(item, ensure_ascii=False) for item in obj) + "]"
        return super().encode(obj)

    def iterencode(self, obj, _one_shot=False):
        """自定义迭代编码"""
        if isinstance(obj, dict):
            yield "{\n"
            self.current_indent += 1
            indent_str = "  " * self.current_indent

            items = list(obj.items())
            for i, (key, value) in enumerate(items):
                yield indent_str + json.dumps(key, ensure_ascii=False) + ": "

                if isinstance(value, dict):
                    yield from self.iterencode(value)
                elif isinstance(value, list):
                    # 检查是否是简单数组
                    if all(isinstance(item, (int, float, str, bool)) or item is None for item in value):
                        yield "[" + ", ".join(json.dumps(item, ensure_ascii=False) for item in value) + "]"
                    else:
                        yield from self.iterencode(value)
                else:
                    yield json.dumps(value, ensure_ascii=False)

                if i < len(items) - 1:
                    yield ","
                yield "\n"

            self.current_indent -= 1
            yield "  " * self.current_indent + "}"

        elif isinstance(obj, list):
            if len(obj) == 0:
                yield "[]"
                return

            # 检查是否是简单数组
            if all(isinstance(item, (int, float, str, bool)) or item is None for item in obj):
                yield "[" + ", ".join(json.dumps(item, ensure_ascii=False) for item in obj) + "]"
                return

            yield "[\n"
            self.current_indent += 1
            indent_str = "  " * self.current_indent

            for i, item in enumerate(obj):
                yield indent_str
                yield from self.iterencode(item)
                if i < len(obj) - 1:
                    yield ","
                yield "\n"

            self.current_indent -= 1
            yield "  " * self.current_indent + "]"
        else:
            yield json.dumps(obj, ensure_ascii=False)


def format_json_compact(obj):
    """使用紧凑格式格式化JSON"""
    encoder = CompactJSONEncoder(ensure_ascii=False)
    return ''.join(encoder.iterencode(obj))


block_state = {
    "variants": {
        "facing=north": {
            "model": "kaleidoscope_doll:block/doll/doll_0"
        },
        "facing=east": {
            "model": "kaleidoscope_doll:block/doll/doll_0",
            "y": 90
        },
        "facing=south": {
            "model": "kaleidoscope_doll:block/doll/doll_0",
            "y": 180
        },
        "facing=west": {
            "model": "kaleidoscope_doll:block/doll/doll_0",
            "y": 270
        }
    }
}


def chinese_to_english_lowercase(text):
    """
    将中文字符转换为英文小写
    """
    # 如果全是英文字符，直接转小写
    if text.isascii():
        return text.lower()

    # 转换中文为拼音
    pinyin_list = pinyin(text, style=Style.NORMAL, heteronym=False)
    # 提取拼音并连接
    result = ''.join([item[0] for item in pinyin_list])

    # 只保留字母和数字，其他字符用下划线替换
    result = re.sub(r'[^a-zA-Z0-9]', '_', result)

    # 转换为小写
    result = result.lower()

    # 移除多余的下划线
    result = re.sub(r'_+', '_', result)
    result = result.strip('_')

    return result


if __name__ == "__main__":
    index = 267
    lang = {}
    register_tooltips = []

    # 清空并重新创建 output 文件夹
    if os.path.exists("output"):
        shutil.rmtree("output")

    # 创建必要的文件夹结构
    os.makedirs("output/blockstates", exist_ok=True)
    os.makedirs("output/models", exist_ok=True)
    os.makedirs("output/textures", exist_ok=True)
    for pack_dir in os.listdir("./doll"):
        model_file = "./doll/" + pack_dir + "/assets/minecraft/models/item/totem_of_undying.json"
        model_texture = "./doll/" + pack_dir + "/assets/minecraft/textures/item/totem/totem_of_undying.png"
        if os.path.exists(model_file):
            file_name = "doll_" + str(index)
            with open(model_file, "r") as f:
                model_obj = json.load(f)
                model_obj["render_type"] = "cutout"
                for key in model_obj["textures"].keys():
                    if key == "particle":
                        model_obj["textures"][key] = "block/white_wool"
                    else:
                        model_obj["textures"][key] = "kaleidoscope_doll:block/doll/" + file_name

            # 保存模型文件到 models 文件夹，格式化输出
            with open("output/models/" + file_name + ".json", "w", encoding='utf-8') as f:
                f.write(format_json_compact(model_obj))

            # 复制材质文件到 textures 文件夹
            shutil.copyfile(model_texture, "output/textures/" + file_name + ".png")

            # 将中文字符转换成小写英文字符
            english_pack_dir = chinese_to_english_lowercase(pack_dir)

            # 收集注册代码和语言文件
            tooltip_code = "registerSpecialTooltips(\"{}\",\"sponsors_{}\");".format(file_name, english_pack_dir)
            register_tooltips.append(tooltip_code)
            lang["tooltip.kaleidoscope_doll.doll.sponsors_{}".format(english_pack_dir)] = "Sponsors: {}".format(
                pack_dir)

            index = index + 1

            # 保存 blockstate 文件，格式化输出
            with open("output/blockstates/" + file_name + ".json", "w", encoding='utf-8') as f:
                tmp = copy.deepcopy(block_state)
                for variants in tmp["variants"].values():
                    variants["model"] = "kaleidoscope_doll:block/doll/" + file_name
                f.write(format_json_compact(tmp))

    # 输出语言文件
    with open("output/lang.json", "w", encoding='utf-8') as f:
        f.write(format_json_compact(lang))

    # 输出注册代码文件
    with open("output/register_tooltips.txt", "w", encoding='utf-8') as f:
        for tooltip in register_tooltips:
            f.write(tooltip + "\n")

    print("=" * 50)
    print("脚本执行完成！")
    print(f"共处理了 {len(register_tooltips)} 个文件")
    print("生成的文件：")
    print("- output/models/        模型文件")
    print("- output/textures/      材质文件")
    print("- output/blockstates/   方块状态文件")
    print("- output/lang.json      语言文件")
    print("- output/register_tooltips.txt  注册代码")
    print("=" * 50)
