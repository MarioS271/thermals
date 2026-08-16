cmake_minimum_required(VERSION 4.2)
project(pawnio_reader)

set(CMAKE_CXX_STANDARD 23)

add_executable(pawnio_reader
    src/main.cpp
    src/cpu.cpp
    src/gpu.cpp
    src/json.cpp
    src/util.cpp
)

target_include_directories(pawnio_reader PRIVATE lib lib/nvapi include)

target_link_libraries(pawnio_reader PRIVATE
    ${CMAKE_SOURCE_DIR}/lib/PawnIOLib.lib
    ${CMAKE_SOURCE_DIR}/lib/nvapi/amd64/nvapi64.lib
    gdi32
)

add_custom_command(TARGET pawnio_reader POST_BUILD
    COMMAND ${CMAKE_COMMAND} -E copy_if_different
    ${CMAKE_SOURCE_DIR}/../resources/windows/PawnIOLib.dll
    $<TARGET_FILE_DIR:pawnio_reader>
)
add_custom_command(TARGET pawnio_reader POST_BUILD
    COMMAND ${CMAKE_COMMAND} -E copy_directory
    ${CMAKE_SOURCE_DIR}/../resources/windows/modules/
    $<TARGET_FILE_DIR:pawnio_reader>/modules
)
