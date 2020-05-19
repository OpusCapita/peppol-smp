import Countries from "./Countries";

const BusinessPlatforms = [
    {name: "A2A"},
    {name: "XIB"},
    {name: "SIRIUS"},
];

BusinessPlatforms.findByName = function (name) {
    return BusinessPlatforms.find(x => x.name === name);
};

export default BusinessPlatforms;

