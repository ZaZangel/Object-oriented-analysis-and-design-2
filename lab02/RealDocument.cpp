#include "RealDocument.hpp"

RealDocument::RealDocument(const std::string& fname, const std::string& desc)
    : fileName(fname), description(desc)
{
    std::cout << "  [Real] Created: " << fileName << std::endl;
}

void RealDocument::Load()
{
    if (isLoaded) return;

    std::cout << "  [Real] Loading " << fileName << "..." << std::endl;
    std::this_thread::sleep_for(std::chrono::milliseconds(1500));

    content = "Content: " + fileName + "\nDescription: " + description;
    isLoaded = true;

    std::cout << "  [Real] Loaded!" << std::endl;
}

void RealDocument::Display()
{
    if (!isLoaded) Load();
    std::cout << "  [Real] >>> " << content << std::endl;
}

std::string RealDocument::GetInfo()
{
    if (!isLoaded) Load();
    return "File: " + fileName + ", Size: " + std::to_string(fileSize) +
        " MB, Description: " + description;
}

std::string RealDocument::GetFileName() const
{
    return fileName;
}

std::string RealDocument::GetDescription() const
{
    return description;
}

bool RealDocument::IsLoaded() const
{
    return isLoaded;
}