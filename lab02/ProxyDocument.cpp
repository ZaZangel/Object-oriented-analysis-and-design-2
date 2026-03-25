#include "ProxyDocument.hpp"

ProxyDocument::ProxyDocument(const std::string& fname, const std::string& desc)
    : fileName(fname), description(desc)
{
    std::cout << "  [Proxy] Created for: " << fileName << std::endl;
}

void ProxyDocument::Load()
{
    if (!realDoc)
    {
        std::cout << "  [Proxy] Creating real document..." << std::endl;
        realDoc = std::make_unique<RealDocument>(fileName, description);
    }
    realDoc->Load();
    isLoaded = true;
    accessCount++;
}

void ProxyDocument::Display()
{
    std::cout << "  [Proxy] Request #" << (accessCount + 1) << std::endl;

    if (!isLoaded)
    {
        std::cout << "  [Proxy] First load (slow)..." << std::endl;
        Load();
    }
    else
    {
        std::cout << "  [Proxy] Using cache (fast)!" << std::endl;
    }

    realDoc->Display();
    accessCount++;
}

std::string ProxyDocument::GetInfo()
{
    if (realDoc && isLoaded)
        return realDoc->GetInfo();
    return fileName + " (not loaded)";
}

std::string ProxyDocument::GetFileName() const
{
    return fileName;
}

std::string ProxyDocument::GetDescription() const
{
    return description;
}

bool ProxyDocument::IsLoaded() const
{
    return isLoaded;
}

std::string ProxyDocument::GetStatistics() const
{
    return "Statistics:\n"
        "  File: " + fileName + "\n"
        "  Loaded: " + std::string(isLoaded ? "Yes" : "No") + "\n"
        "  Access count: " + std::to_string(accessCount);
}