(ns clojure-poi.core
  (:gen-class))

(import '(org.apache.poi.xssf.usermodel
           XSSFSheet
           XSSFWorkbook
           XSSFRow
           XSSFCell)
  '(org.apache.poi.ss.usermodel
     WorkbookFactory))

(import '(java.io
          FileInputStream
          FileOutputStream))

(defn load-workbook [path]
  (-> path FileInputStream. WorkbookFactory/create))

;; (def rows (iterator-seq (.rowIterator sheet0)))
;; (def row0 (first rows))
;; (def cells (iterator-seq (.cellIterator row0)))
;; (map #(-> % .getRichStringCellValue .getString) cells)


