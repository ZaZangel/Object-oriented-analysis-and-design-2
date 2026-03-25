#ifndef PROXYDOCUMENT_HPP
#define PROXYDOCUMENT_HPP

#include "IDocument.hpp"
#include "RealDocument.hpp"
#include <memory>
#include <string>
#include <iostream>

class ProxyDocument : public IDocument
{
private:
    std::string fileName;
    std::string description;
    std::unique_ptr<RealDocument> realDoc;
    bool isLoaded = false;
    int accessCount = 0;

public:
    ProxyDocument(const std::string& fname, const std::string& desc);

    void Load() override;
    void Display() override;
    std::string GetInfo() override;
    std::string GetFileName() const override;
    std::string GetDescription() const override;
    bool IsLoaded() const override;
    int GetAccessCount() const { return accessCount; }
    std::string GetStatistics() const;
};

#endif // PROXYDOCUMENT_HPP