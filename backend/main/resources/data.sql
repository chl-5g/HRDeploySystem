-- ID3 training samples (loaded on startup if table is empty)
INSERT IGNORE INTO training_sample (category, qual, major_bucket, graduate_tier, label) VALUES
('理工类', '专科', 'it', 'normal', '研发部|1'),
('理工类', '本科', 'it', 'normal', '研发部|3'),
('理工类', '硕士', 'it', 'top', '研发部|5'),
('理工类', '博士', 'it', 'top', '研发部|6'),
('理工类', '本科', 'other', 'normal', '研发部|2'),
('理工类', '硕士', 'other', 'normal', '研发部|3'),
('经济管理类', '专科', 'finance', 'normal', '财务审计部|1'),
('经济管理类', '本科', 'finance', 'normal', '财务审计部|2'),
('经济管理类', '硕士', 'finance', 'top', '财务审计部|4'),
('经济管理类', '本科', 'market', 'normal', '市场营销部|2'),
('经济管理类', '硕士', 'market', 'normal', '市场营销部|3'),
('经济管理类', '本科', 'hr', 'normal', '人力资源部|2'),
('经济管理类', '硕士', 'hr', 'top', '人力资源部|4'),
('其他', '专科', 'other', 'normal', '售后服务部|1'),
('其他', '本科', 'other', 'normal', '售后服务部|2'),
('其他', '硕士', 'other', 'normal', '售后服务部|3'),
('其他', '博士', 'other', 'top', '售后服务部|4');
