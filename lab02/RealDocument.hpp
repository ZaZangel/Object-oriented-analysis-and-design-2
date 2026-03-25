#ifndef REALDOCUMENT_HPP
#define REALDOCUMENT_HPP

#include "IDocument.hpp"
#include <string>
#include <iostream>
#include <thread>
#include <chrono>

class RealDocument : public IDocument
{
private:
    std::string fileName;
    std::string description;
    std::string content;
    bool isLoaded = false;
    long fileSize = 15;

public:
    RealDocument(const std::string& fname, const std::string& desc);

    void Load() override;
    void Display() override;
    std::string GetInfo() override;
    std::string GetFileName() const override;
    std::string GetDescription() const override;
    bool IsLoaded() const override;
    long GetSize() const { return fileSize; }
};

#endif // REALDOCUMENT_HPP