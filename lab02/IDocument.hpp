#ifndef IDOCUMENT_HPP
#define IDOCUMENT_HPP

#include <string>

class IDocument
{
public:
    virtual ~IDocument() = default;
    virtual void Load() = 0;
    virtual void Display() = 0;
    virtual std::string GetInfo() = 0;
    virtual std::string GetFileName() const = 0;
    virtual std::string GetDescription() const = 0;
    virtual bool IsLoaded() const = 0;
};

#endif // IDOCUMENT_HPP