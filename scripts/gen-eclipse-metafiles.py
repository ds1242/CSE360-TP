#!/usr/bin/env python3

import sys
import tomllib
from pathlib import Path


PROJECT_NAME = "CSE360-TP"

project_lines = [
    '<?xml version="1.0" encoding="UTF-8"?>',
    '<projectDescription>',
    f'    <name>{PROJECT_NAME}</name>',
    '    <buildSpec>',
    '        <buildCommand>',
    '            <name>org.eclipse.jdt.core.javabuilder</name>',
    '        </buildCommand>',
    '    </buildSpec>',
    '<natures>',
    '    <nature>org.eclipse.jdt.core.javanature</nature>',
    '</natures>',
    '</projectDescription>',
]

classpath_lines = [
    '<?xml version="1.0" encoding="UTF-8"?>',
    '<classpath>',
    '    <classpathentry kind="src" path="src"/>',
    '    <classpathentry kind="output" path="bin"/>',
    '    <classpathentry kind="con" path="org.eclipse.jdt.launching.JRE_CONTAINER"/>',
]

def add(jar, src):
    classpath_lines.extend([
        f'    <classpathentry kind="lib" path="{jar}" sourcepath="{src}">',
         '        <attributes><attribute name="module" value="true"/></attributes>',
         '    </classpathentry>',
    ])

if __name__ == "__main__":
    program = sys.argv[0]
    name = Path(program).name
    if len(sys.argv) > 1:
        sys.stderr.write(f"{name}: no args expected\n")
        sys.exit(1)

    rootdir = Path(__file__).resolve().parent.parent
    manifest = rootdir / "lib" / "manifest.toml"
    if not manifest.exists():
        sys.stderr.write(f"{name}: no lib/manifest.toml found\n")
        sys.exit(1)

    with open(manifest, "rb") as f:
        tomldata = tomllib.load(f)

    project_out = rootdir / ".project"
    if project_out.exists():
        project_out.move(project_out.with_suffix(".bak"))
    with open(project_out, "w") as f:
        f.write("\n".join(project_lines))

    for library in tomldata.get("lib", []):
        src = library["src"]
        for jar in library["jars"]:
            add(jar, src)

    classpath_lines.extend(["</classpath>"])

    out = rootdir / ".classpath"
    if out.exists():
        out.move(out.with_suffix(".bak"))

    with open(out, "w") as f:
        f.write("\n".join(classpath_lines))

    sys.stdout.write(f"{project_out.relative_to(rootdir)} written \n")
    sys.stdout.write(f"{out.relative_to(rootdir)} written \n")
    sys.exit(0)
